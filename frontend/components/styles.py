"""CSS styling for the banking application."""
import streamlit as st


def apply_custom_styles():
    """Apply custom CSS styles to the application."""
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
