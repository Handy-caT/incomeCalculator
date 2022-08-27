import sqlalchemy

metadata = sqlalchemy.MetaData()

items_table = sqlalchemy.Table(
    'Items',
    metadata,
    sqlalchemy.Column('item_id',)
)