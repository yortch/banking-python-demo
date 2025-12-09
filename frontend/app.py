import streamlit as st
import requests
import pandas as pd
from datetime import datetime

# API Base URL - can be overridden via environment variable
import os
API_BASE_URL = os.getenv("API_BASE_URL", "http://localhost:8080/api")

# Simulated logged-in user
LOGGED_IN_USER = "John Doe"

# Page Configuration
st.set_page_config(
    page_title="Three Rivers Bank",
    page_icon="🏦",
    layout="wide"
)

# Custom CSS for styling
st.markdown("""
    <style>
    .main-header {
        font-size: 2.5rem;
        font-weight: bold;
        color: #1e3a8a;
        text-align: center;
        padding: 1rem 0;
        border-bottom: 3px solid #1e3a8a;
        margin-bottom: 2rem;
    }
    .account-card {
        background-color: #f0f9ff;
        border-radius: 10px;
        padding: 1.5rem;
        border-left: 5px solid #1e3a8a;
        margin: 1rem 0;
    }
    .balance {
        font-size: 2rem;
        font-weight: bold;
        color: #1e3a8a;
    }
    .account-type {
        color: #64748b;
        font-size: 1rem;
    }
    .user-info {
        background-color: #dbeafe;
        padding: 1rem;
        border-radius: 5px;
        margin-bottom: 1rem;
    }
    </style>
""", unsafe_allow_html=True)

def get_accounts():
    """Fetch all accounts from the API"""
    try:
        response = requests.get(f"{API_BASE_URL}/accounts")
        if response.status_code == 200:
            return response.json()
        return []
    except Exception as e:
        st.error(f"Error fetching accounts: {str(e)}")
        return []

def get_account_details(account_number):
    """Fetch specific account details"""
    try:
        response = requests.get(f"{API_BASE_URL}/accounts/{account_number}")
        if response.status_code == 200:
            return response.json()
        return None
    except Exception as e:
        st.error(f"Error fetching account details: {str(e)}")
        return None

def get_transactions(account_number):
    """Fetch transactions for an account"""
    try:
        response = requests.get(f"{API_BASE_URL}/transactions/{account_number}")
        if response.status_code == 200:
            return response.json()
        return []
    except Exception as e:
        st.error(f"Error fetching transactions: {str(e)}")
        return []

def transfer_funds(from_account, to_account, amount, description):
    """Transfer funds between accounts"""
    try:
        payload = {
            "fromAccount": from_account,
            "toAccount": to_account,
            "amount": str(amount),
            "description": description
        }
        response = requests.post(f"{API_BASE_URL}/transfer", json=payload)
        if response.status_code == 200:
            return True, "Transfer successful!"
        else:
            error_data = response.json()
            return False, error_data.get("error", "Transfer failed")
    except Exception as e:
        return False, f"Error: {str(e)}"

# Header
st.markdown('<div class="main-header">🏦 Three Rivers Bank - Online Banking</div>', unsafe_allow_html=True)

# Display logged-in user
st.markdown(f'<div class="user-info">Welcome back, <strong>{LOGGED_IN_USER}</strong>! 👋</div>', unsafe_allow_html=True)

# Navigation
page = st.sidebar.radio("Navigation", ["Dashboard", "Account Details", "Transactions", "Transfer Funds"])

if page == "Dashboard":
    st.header("Account Overview")
    
    accounts = get_accounts()
    
    if accounts:
        cols = st.columns(len(accounts))
        for idx, account in enumerate(accounts):
            with cols[idx]:
                st.markdown(f"""
                    <div class="account-card">
                        <div class="account-type">{account['accountType']}</div>
                        <div>Account: {account['accountNumber']}</div>
                        <div class="balance">${account['balance']:,.2f}</div>
                    </div>
                """, unsafe_allow_html=True)
        
        # Total balance
        total_balance = sum(float(acc['balance']) for acc in accounts)
        st.markdown("---")
        st.metric("Total Balance", f"${total_balance:,.2f}")
    else:
        st.warning("No accounts found")

elif page == "Account Details":
    st.header("Account Details")
    
    accounts = get_accounts()
    
    if accounts:
        account_options = {f"{acc['accountType']} - {acc['accountNumber']}": acc['accountNumber'] 
                          for acc in accounts}
        selected_display = st.selectbox("Select Account", list(account_options.keys()))
        selected_account = account_options[selected_display]
        
        account = get_account_details(selected_account)
        
        if account:
            col1, col2 = st.columns(2)
            
            with col1:
                st.subheader("Account Information")
                st.write(f"**Account Number:** {account['accountNumber']}")
                st.write(f"**Account Type:** {account['accountType']}")
                st.write(f"**Customer Name:** {account['customerName']}")
            
            with col2:
                st.subheader("Balance")
                balance = float(account['balance'])
                if balance >= 0:
                    st.success(f"${balance:,.2f}")
                else:
                    st.error(f"${balance:,.2f}")

elif page == "Transactions":
    st.header("Transaction History")
    
    accounts = get_accounts()
    
    if accounts:
        account_options = {f"{acc['accountType']} - {acc['accountNumber']}": acc['accountNumber'] 
                          for acc in accounts}
        selected_display = st.selectbox("Select Account", list(account_options.keys()))
        selected_account = account_options[selected_display]
        
        transactions = get_transactions(selected_account)
        
        if transactions:
            # Format transactions for display
            df_data = []
            for txn in transactions:
                # Parse timestamp
                timestamp = datetime.fromisoformat(txn['timestamp'].replace('Z', '+00:00'))
                
                # Determine if incoming or outgoing
                if txn['toAccountNumber'] == selected_account:
                    direction = "Incoming"
                    other_account = txn['fromAccountNumber']
                else:
                    direction = "Outgoing"
                    other_account = txn['toAccountNumber']
                
                df_data.append({
                    "Date": timestamp.strftime("%Y-%m-%d %H:%M"),
                    "Type": txn['type'],
                    "Direction": direction,
                    "Other Account": other_account,
                    "Amount": float(txn['amount']),
                    "Description": txn['description']
                })
            
            df = pd.DataFrame(df_data)
            st.dataframe(df, use_container_width=True, hide_index=True)
        else:
            st.info("No transactions found for this account")

elif page == "Transfer Funds":
    st.header("Transfer Between Accounts")
    
    accounts = get_accounts()
    
    if accounts and len(accounts) >= 2:
        account_options = {f"{acc['accountType']} - {acc['accountNumber']}": acc['accountNumber'] 
                          for acc in accounts}
        
        col1, col2 = st.columns(2)
        
        with col1:
            from_display = st.selectbox("From Account", list(account_options.keys()))
            from_account = account_options[from_display]
        
        with col2:
            # Filter out the from_account from to_account options
            to_options = {k: v for k, v in account_options.items() if v != from_account}
            to_display = st.selectbox("To Account", list(to_options.keys()))
            to_account = to_options[to_display]
        
        amount = st.number_input("Amount", min_value=0.01, step=0.01, format="%.2f")
        description = st.text_input("Description (optional)", value="Internal Transfer")
        
        if st.button("Transfer", type="primary"):
            if amount > 0:
                success, message = transfer_funds(from_account, to_account, amount, description)
                if success:
                    st.success(message)
                    st.balloons()
                else:
                    st.error(message)
            else:
                st.error("Amount must be greater than 0")
    else:
        st.warning("You need at least 2 accounts to perform transfers")

# Footer
st.sidebar.markdown("---")
st.sidebar.markdown("**Three Rivers Bank**")
st.sidebar.markdown("_Serving communities since 1975_")
