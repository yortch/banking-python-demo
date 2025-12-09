"""Data formatting utilities."""
from datetime import datetime
from typing import List, Dict
import pandas as pd


def format_currency(amount: float) -> str:
    """
    Format a number as currency.
    
    Args:
        amount: The amount to format
        
    Returns:
        str: Formatted currency string
    """
    return f"${amount:,.2f}"


def format_datetime(timestamp: str, format_string: str = "%Y-%m-%d %H:%M") -> str:
    """
    Format an ISO timestamp string.
    
    Args:
        timestamp: ISO format timestamp string
        format_string: Desired output format
        
    Returns:
        str: Formatted datetime string
    """
    dt = datetime.fromisoformat(timestamp.replace('Z', '+00:00'))
    return dt.strftime(format_string)


def format_transactions_for_display(
    transactions: List[Dict], 
    selected_account: str
) -> pd.DataFrame:
    """
    Format transaction data for display in a DataFrame.
    
    Args:
        transactions: List of transaction dictionaries
        selected_account: The account number to determine transaction direction
        
    Returns:
        pd.DataFrame: Formatted transactions DataFrame
    """
    df_data = []
    for txn in transactions:
        # Determine if incoming or outgoing
        if txn['toAccountNumber'] == selected_account:
            direction = "Incoming"
            other_account = txn['fromAccountNumber']
        else:
            direction = "Outgoing"
            other_account = txn['toAccountNumber']
        
        df_data.append({
            "Date": format_datetime(txn['timestamp']),
            "Type": txn['type'],
            "Direction": direction,
            "Other Account": other_account,
            "Amount": float(txn['amount']),
            "Description": txn['description']
        })
    
    return pd.DataFrame(df_data)
