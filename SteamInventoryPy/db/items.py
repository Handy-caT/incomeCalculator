from sqlalchemy import Column, Table, DateTime, BIGINT, VARCHAR
from sqlalchemy.sql import func

from db.engine import Engine

metadata = Engine.get_metadata()

items_table = Table(
    'Items',
    metadata,
    Column('id', BIGINT, primary_key=True),
    Column('class_id', BIGINT, unique=True, nullable=False, index=True),
    Column('item_name', VARCHAR(40), nullable=False),
    Column('created_timestamp', DateTime, server_default=func.now()),
    Column('updated_timestamp', DateTime, onupdate=func.now())
)
