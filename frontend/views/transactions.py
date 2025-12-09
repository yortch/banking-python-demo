"""Transaction history page."""
import streamlit as st
from services.api_client import get_accounts, get_transactions
from utils.formatters import format_transactions_for_display


def render():
    """Render the transaction history page."""
    st.header("Transaction History")
    
    accounts = get_accounts()
    
    if accounts:
        account_options = {
            f"{acc['accountType']} - {acc['accountNumber']}": acc['accountNumber'] 
            for acc in accounts
        }
        selected_display = st.selectbox("Select Account", list(account_options.keys()))
        selected_account = account_options[selected_display]
        
        transactions = get_transactions(selected_account)
        
        if transactions:
            df = format_transactions_for_display(transactions, selected_account)
            st.dataframe(df, use_container_width=True, hide_index=True)
        else:
            st.info("No transactions found for this account")
