from sqlalchemy import text


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
    return connection.execute(f'CREATE TABLE {table_name} (id BIGINT AUTO_INCREMENT PRIMARY KEY)')


def create_table(table_name, connection):
    script = text(read_script('sql/create' + table_name + '.sql'))
    return connection.execute(script)


def drop_table(table_name, connection):
    return connection.execute(f'DROP TABLE IF EXISTS {table_name}')


def add_column_null(table_name, column_name, column_type, connection):
    return connection.execute(f'ALTER TABLE {table_name} ADD COLUMN {column_name} {column_type} NULL')


def add_column_not_null(table_name, column_name, column_type, connection):
    for each in default_values:
        if each in column_type:
            return connection.execute(f'ALTER TABLE {table_name} ADD COLUMN {column_name} {column_type} '
                                      f'NOT NULL DEFAULT {default_values[each]}')


def drop_column(table_name, column_name, connection):
    return connection.execute(f'ALTER TABLE {table_name} DROP COLUMN {column_name}')


def add_unique_constraint(table_name, column_name, connection):
    return connection.execute(f'ALTER TABLE {table_name} ADD CONSTRAINT {table_name}_unique UNIQUE ({column_name})')


def add_foreign_key(table_name, column_name, foreign_key_table_name, foreign_key_column_name, connection):
    return connection.execute(
        f'ALTER TABLE {table_name} ADD CONSTRAINT {table_name}_foreign_key FOREIGN KEY ({column_name}) '
        f'REFERENCES {foreign_key_table_name}({foreign_key_column_name})')


def check_if_table_exists(connection, table_name):
    return connection.dialect.has_table(connection, table_name)


def check_if_column_exists(connection, table_name, column_name):
    return connection.execute(f'SHOW COLUMNS FROM {table_name} LIKE "{column_name}"')
