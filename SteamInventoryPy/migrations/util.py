import configparser

from pathlib import Path
from sqlalchemy import text

from db.engine import Engine

configs_dir = 'migrations/configs'


def make_dir():
    Path(configs_dir).mkdir(parents=True, exist_ok=True)


def get_config():
    config = configparser.ConfigParser()
    config.read(configs_dir+'/migrations.ini')

    return config


def check_table_exists(engine, table_name):
    with engine.connect() as connection:
        return engine.dialect.has_table(connection, table_name)


def save_table_state(table_name, version, state):
    config = get_config()
    config[version] = {}
    config[version][table_name] = state

    with open(configs_dir+'/migrations.ini', 'w') as configfile:
        config.write(configfile)
