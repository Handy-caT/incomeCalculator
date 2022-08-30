from sqlalchemy import text

from db.engine import Engine

default_values = {
    'BIT': 'False',
    'INT': '0',
    'VARCHAR': '_'
}


def read_script(filename):
    file = open(filename, 'r')
    script = file.read()
    file.close()

    return script


def create_blank_table(table_name, connection):
    connection.execute(f'CREATE TABLE {table_name} (id BIGINT AUTO_INCREMENT PRIMARY KEY)')


def create_table(table_name, connection):
    script = text(read_script('sql/create'+table_name+'.sql'))
    connection.execute(script)


def drop_table(table_name, connection):
    connection.execute(f'DROP TABLE IF EXISTS {table_name}')


def add_column_null(table_name, column_name, column_type, connection):
    connection.execute(f'ALTER TABLE {table_name} ADD COLUMN {column_name} {column_type} NULL')


def add_column_not_null(table_name, column_name, column_type, connection):
    for each in default_values:
        if each in column_type:
            connection.execute(f'ALTER TABLE {table_name} ADD COLUMN {column_name} {column_type} '
                               f'NOT NULL DEFAULT {default_values[each]}')
            return


def drop_column(table_name, column_name, connection):
    connection.execute(f'ALTER TABLE {table_name} DROP COLUMN {column_name}')


def add_unique_constraint(table_name, column_name, connection):
    connection.execute(f'ALTER TABLE {table_name} ADD CONSTRAINT {table_name}_unique UNIQUE ({column_name})')


def add_foreign_key(table_name, column_name, foreign_key_table_name, foreign_key_column_name, connection):
    connection.execute(f'ALTER TABLE {table_name} ADD CONSTRAINT {table_name}_foreign_key FOREIGN KEY ({column_name}) '
                       f'REFERENCES {foreign_key_table_name}({foreign_key_column_name})')
