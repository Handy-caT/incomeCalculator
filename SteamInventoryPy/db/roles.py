from sqlalchemy import Column, Table, Integer, String

from db.engine import Engine

metadata = Engine.get_metadata()

roles_table = Table(
    'Roles',
    metadata,
    Column('id', Integer, primary_key=True),
    Column('role_name', String(255)),
)
