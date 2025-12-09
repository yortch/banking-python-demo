"""Header and navigation components."""
import streamlit as st
from config.settings import LOGGED_IN_USER, BANK_NAME, BANK_TAGLINE


def render_header():
    """Render the main application header."""
    st.markdown(
        f'<div class="main-header">🏦 {BANK_NAME} - Online Banking</div>', 
        unsafe_allow_html=True
    )


def render_user_info():
    """Render the logged-in user information."""
    st.markdown(
        f'<div class="user-info">Welcome back, <strong>{LOGGED_IN_USER}</strong>! 👋</div>', 
        unsafe_allow_html=True
    )


def render_sidebar_footer():
    """Render the sidebar footer with bank information."""
    st.sidebar.markdown("---")
    st.sidebar.markdown(f"**{BANK_NAME}**")
    st.sidebar.markdown(f"_{BANK_TAGLINE}_")
