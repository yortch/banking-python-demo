"""Transfer funds page."""
import streamlit as st
from services.api_client import get_accounts, transfer_funds


def render():
    """Render the transfer funds page."""
    st.header("Transfer Between Accounts")
    
    accounts = get_accounts()
    
    if accounts and len(accounts) >= 2:
        account_options = {
            f"{acc['accountType']} - {acc['accountNumber']}": acc['accountNumber'] 
            for acc in accounts
        }
        
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
