from pathlib import Path
from sqlalchemy import text

from db.engine import Engine


def read_script(filename):
    file = open(filename, 'r')
    script = file.read()
    file.close()

    return script


def make_dir():
    Path('migrations/configs').mkdir(parents=True, exist_ok=True)


def check_table_exists(engine, table_name):
    with engine.connect() as connection:
        return engine.dialect.has_table(connection, table_name)


def create_table(table_name):
    script = text(read_script('../sql/create'+table_name+'.sql'))
    connection = Engine.get_connection()

    result = connection.execute(script)
    connection.close()
    
    return result


