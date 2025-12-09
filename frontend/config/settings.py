"""Application configuration and settings."""
import os

# API Configuration
API_BASE_URL = os.getenv("API_BASE_URL", "http://localhost:8080/api")

# User Configuration
LOGGED_IN_USER = "John Doe"

# Page Configuration
PAGE_TITLE = "Three Rivers Bank"
PAGE_ICON = "🏦"
LAYOUT = "wide"

# Bank Information
BANK_NAME = "Three Rivers Bank"
BANK_TAGLINE = "Serving communities since 1975"
