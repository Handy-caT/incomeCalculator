from sqlalchemy import Column, Table, BIGINT, VARCHAR

from db.engine import Engine

metadata = Engine.get_metadata()

roles_table = Table(
    'Roles',
    metadata,
    Column('id', BIGINT, primary_key=True),
    Column('role_name', VARCHAR(255)),
)
