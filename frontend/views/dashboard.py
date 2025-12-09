"""Dashboard page - Account overview."""
import streamlit as st
from services.api_client import get_accounts
from utils.formatters import format_currency


def render():
    """Render the dashboard page."""
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
                        <div class="balance">{format_currency(float(account['balance']))}</div>
                    </div>
                """, unsafe_allow_html=True)
        
        # Total balance
        total_balance = sum(float(acc['balance']) for acc in accounts)
        st.markdown("---")
        st.metric("Total Balance", format_currency(total_balance))
    else:
        st.warning("No accounts found")
