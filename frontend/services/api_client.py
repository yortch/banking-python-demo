"""API client for communication with the banking backend."""
import requests
import streamlit as st
from typing import List, Dict, Optional, Tuple
from config.settings import API_BASE_URL


def get_accounts() -> List[Dict]:
    """
    Fetch all accounts from the API.
    
    Returns:
        List[Dict]: List of account dictionaries, or empty list if error occurs
    """
    try:
        response = requests.get(f"{API_BASE_URL}/accounts")
        if response.status_code == 200:
            return response.json()
        return []
    except Exception as e:
        st.error(f"Error fetching accounts: {str(e)}")
        return []


def get_account_details(account_number: str) -> Optional[Dict]:
    """
    Fetch specific account details.
    
    Args:
        account_number: The account number to fetch details for
        
    Returns:
        Optional[Dict]: Account details dictionary, or None if error occurs
    """
    try:
        response = requests.get(f"{API_BASE_URL}/accounts/{account_number}")
        if response.status_code == 200:
            return response.json()
        return None
    except Exception as e:
        st.error(f"Error fetching account details: {str(e)}")
        return None


def get_transactions(account_number: str) -> List[Dict]:
    """
    Fetch transactions for an account.
    
    Args:
        account_number: The account number to fetch transactions for
        
    Returns:
        List[Dict]: List of transaction dictionaries, or empty list if error occurs
    """
    try:
        response = requests.get(f"{API_BASE_URL}/transactions/{account_number}")
        if response.status_code == 200:
            return response.json()
        return []
    except Exception as e:
        st.error(f"Error fetching transactions: {str(e)}")
        return []


def transfer_funds(
    from_account: str, 
    to_account: str, 
    amount: float, 
    description: str
) -> Tuple[bool, str]:
    """
    Transfer funds between accounts.
    
    Args:
        from_account: Source account number
        to_account: Destination account number
        amount: Transfer amount
        description: Transfer description
        
    Returns:
        Tuple[bool, str]: (success status, message)
    """
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
