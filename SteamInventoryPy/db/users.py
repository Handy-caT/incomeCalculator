from sqlalchemy import Column, Table,  BIGINT, VARCHAR, DateTime, Boolean, ForeignKey
from sqlalchemy.sql import func

from db.engine import Engine

metadata = Engine.get_metadata()

users_table = Table(
    'Users',
    metadata,
    Column('id', BIGINT, primary_key=True),
    Column('login', VARCHAR(255)),
    Column('password', VARCHAR(255)),
    Column('registration_date_time', DateTime, server_default=func.now()),
    Column('last_update_date_time', DateTime, onupdate=func.now()),
    Column('is_using_cookies', Boolean, nullable=False),
    Column('role_id', BIGINT, ForeignKey('Roles.id'))
)
