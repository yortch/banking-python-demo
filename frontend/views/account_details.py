"""Account details page."""
import streamlit as st
from services.api_client import get_accounts, get_account_details
from utils.formatters import format_currency


def render():
    """Render the account details page."""
    st.header("Account Details")
    
    accounts = get_accounts()
    
    if accounts:
        account_options = {
            f"{acc['accountType']} - {acc['accountNumber']}": acc['accountNumber'] 
            for acc in accounts
        }
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
                    st.success(format_currency(balance))
                else:
                    st.error(format_currency(balance))
