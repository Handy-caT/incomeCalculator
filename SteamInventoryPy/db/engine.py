import configparser

import sqlalchemy
from sqlalchemy import create_engine


class Engine:
    sql_engine = None
    metadata = sqlalchemy.MetaData()

    @classmethod
    def get_engine(cls):
        if cls.sql_engine is None:
            cls.sql_engine = cls.__create_sqlalchemy_engine('db/config.ini')

        return cls.sql_engine

    @classmethod
    def __create_sqlalchemy_engine(cls, path):
        config = configparser.ConfigParser()
        config.read(path)

        print(config['mysql'])

        sqlalchemy_url = f"mysql+pymysql://{config['mysql']['user']}:{config['mysql']['password']}" \
                         f"@{config['mysql']['host']}:" \
                         f"{config['mysql']['port']}/{config['mysql']['database']}"

        return create_engine(sqlalchemy_url, echo=True, pool_size=10, pool_recycle=3600)

    @classmethod
    def get_connection(cls):
        return cls.get_engine().connect()

    @classmethod
    def get_metadata(cls):
        return cls.metadata
