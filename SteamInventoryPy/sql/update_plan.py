import json

from db.engine import Engine
from sql.column_validation import check_type, check_nullable, check_primary_key
from sql.exceptions import IncorrectColumnType, IncorrectNullableState, IncorrectPrimaryKey
from sql.table_scan import file_name, configs_dir, make_dir
from sql.util import check_if_table_exists, check_if_column_exists, get_indexes, get_column

column_name_index = 4


def save_plan(plan, version):
    make_dir()

    json_str = json.dumps(plan, indent=3)
    with open(configs_dir + '/' + version + '.json', 'w') as f:
        f.write(json_str)


def check_index(connection, table_name, column_name):
    db_indexes = get_indexes(connection, table_name).fetchall()

    for index in db_indexes:
        if column_name in index[column_name_index]:
            return True

    return False


def compare_column_plans(connection, table_name, column_name, plan):
    column = get_column(connection, table_name, column_name)

    if check_type(column, plan['type']) is False:
        raise IncorrectColumnType(table_name, column[1], plan['type'])

    if plan['nullable'] != check_nullable(column):
        raise IncorrectNullableState(column_name)

    if plan['primary_key'] != check_primary_key(column):
        raise IncorrectPrimaryKey(column_name)


def scan_columns(connection, table_name, table_schema):
    columns = []

    for column in table_schema['columns'].keys():
        result = check_if_column_exists(connection, table_name, column)
        if result is False:
            plan = {
                column: 'Create'
            }
            columns.append(plan)
        else:
            plan = {
                column: 'Existed'
            }
            columns.append(plan)

            compare_column_plans(connection, table_name, column, table_schema['columns'][column])

    return columns


def save_create_columns_plan(table_schema):
    columns = []

    for column in table_schema['columns'].keys():
        plan = {
            column: 'Create'
        }
        columns.append(plan)

    return columns


def compare_tables():
    tables_plan = []
    connection = Engine.get_connection()

    json_file = open(configs_dir + '/' + file_name)
    data = json.load(json_file)

    for table in data:
        if check_if_table_exists(connection, table['name']) is False:
            columns = save_create_columns_plan(table)

            plan = {
                table['name']: 'Create',
                'columns': columns
            }
            tables_plan.append(plan)
        else:
            columns = scan_columns(connection, table['name'], table)

            ind = False
            for column in columns:
                if list(column.values())[0] == 'Create':
                    ind = True
                    break

            if ind is True:
                plan = {
                    table['name']: 'Update',
                    'columns': columns
                }
                tables_plan.append(plan)
            else:
                plan = {
                    table['name']: 'Existed',
                    'columns': columns
                }
                tables_plan.append(plan)

    return tables_plan
