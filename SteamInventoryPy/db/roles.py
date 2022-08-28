import sqlalchemy
from sqlalchemy import Column, Table, Integer, String


metadata = sqlalchemy.MetaData()

roles_table = Table(
    'Roles',
    metadata,
    Column('id', Integer, primary_key=True),
    Column('role_name', String(255)),
)
