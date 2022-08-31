import json

from db.engine import Engine
from sql.table_scan import file_name, configs_dir, make_dir
from sql.util import check_if_table_exists, check_if_column_exists

column_info = {
    'field': 0,
    'type': 1,
    'null': 2,
    'key': 3,
    'default': 4,
    'extra': 5
}


def save_plan(plan, version):
    make_dir()

    json_str = json.dumps(plan, indent=3)
    with open(configs_dir + '/' + version + '.json', 'w') as f:
        f.write(json_str)


def compare_column_plans(schema, plan):
    var = plan['type'].lower() == schema['type'].lower()
    pass


def scan_columns(connection, table_name, table_schema):
    columns = []

    for column in table_schema['columns'].keys():
        result = check_if_column_exists(connection, table_name, column)
        print(result.rowcount)
        if result.rowcount == 0:
            plan = {
                column: 'Create'
            }
            columns.append(plan)
        else:
            plan = {
                column: 'Existed'
            }
            columns.append(plan)

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
