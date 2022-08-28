from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, DateTime
from sqlalchemy.sql import func

Base = declarative_base()


class Item(Base):
    __tablename__ = 'items'

    item_id = Column(Integer, primary_key=True)
    class_id = Column(Integer, unique=True, nullable=False, index=True),
    item_name = Column(String(40), nullable=False),
    created_timestamp = Column(DateTime, server_default=func.now()),
    updated_timestamp = Column(DateTime, onupdate=func.now())

    def __init__(self):
        pass

    def __init__(self, class_id, item_name):
        self.class_id = class_id
        self.item_name = item_name

    def __repr__(self):
        return f'Item({self.class_id}, {self.item_name})'
