from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, DateTime, Boolean, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func

Base = declarative_base()


class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    login = Column(String(255))
    password = Column(String(255))
    registration_date_time = Column(DateTime, server_default=func.now())
    last_update_date_time = Column(DateTime, onupdate=func.now())
    is_using_cookie = Column(Boolean, nullable=False)
    role_id = Column(Integer, ForeignKey('roles.id'))
    role = relationship("Role")

    def __init__(self):
        pass

    def __init__(self, login, password, is_using_cookies):
        self.login = login
        self.password = password
        self.is_using_cookie = is_using_cookies

    def __init__(self, login, password, is_using_cookies, role):
        self.login = login
        self.password = password
        self.is_using_cookie = is_using_cookies
        self.role_id = role.id

    def __repr__(self):
        return f'User({self.login}, {self.password}, {self.is_using_cookie}, {self.role})'
