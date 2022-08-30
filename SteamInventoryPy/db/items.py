from sqlalchemy import Column, Table, Integer, String, DateTime
from sqlalchemy.sql import func

from db.engine import Engine

metadata = Engine.get_metadata()

items_table = Table(
    'Items',
    metadata,
    Column('id', Integer, primary_key=True),
    Column('class_id', Integer, unique=True, nullable=False, index=True),
    Column('item_name', String(40), nullable=False),
    Column('created_timestamp', DateTime, server_default=func.now()),
    Column('updated_timestamp', DateTime, onupdate=func.now())
)
