import sqlalchemy
from sqlalchemy import Column, Table, Integer, String, DateTime, Boolean, ForeignKey
from sqlalchemy.sql import func

metadata = sqlalchemy.MetaData()

users_table = Table(
    'Users',
    metadata,
    Column('id', Integer, primary_key=True),
    Column('login', String(255)),
    Column('password', String(255)),
    Column('registration_date_time', DateTime, server_default=func.now()),
    Column('last_update_date_time', DateTime, onupdate=func.now()),
    Column('is_using_coolies', Boolean, nullable=False),
    Column('role_id', Integer, ForeignKey('roles.id'))
)
