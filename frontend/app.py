"""
Three Rivers Bank - Online Banking Application
Main entry point for the Streamlit application.
"""
import streamlit as st
from config.settings import PAGE_TITLE, PAGE_ICON, LAYOUT
from components.styles import apply_custom_styles
from components.header import render_header, render_user_info, render_sidebar_footer
from views import dashboard, account_details, transactions, transfer

# Page Configuration
st.set_page_config(
    page_title=PAGE_TITLE,
    page_icon=PAGE_ICON,
    layout=LAYOUT
)

# Apply custom styles
apply_custom_styles()

# Render header and user info
render_header()
render_user_info()

# Navigation
page = st.sidebar.radio(
    "Navigation", 
    ["Dashboard", "Account Details", "Transactions", "Transfer Funds"]
)

# Route to appropriate page
if page == "Dashboard":
    dashboard.render()
elif page == "Account Details":
    account_details.render()
elif page == "Transactions":
    transactions.render()
elif page == "Transfer Funds":
    transfer.render()

# Render sidebar footer
render_sidebar_footer()
