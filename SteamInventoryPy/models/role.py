from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, DateTime
from sqlalchemy.sql import func

Base = declarative_base()


class Role(Base):
    __tablename__ = 'roles'

    id = Column(Integer, primary_key=True)
    role_name = Column(String(255))

    def __init__(self):
        pass

    def __init__(self, role_name):
        self.role_name = role_name

    def __repr__(self):
        return f'Role({self.role_name})'
