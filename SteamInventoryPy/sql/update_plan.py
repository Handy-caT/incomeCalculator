import json

from db.engine import Engine
from sql.table_scan import file_name, configs_dir
from sql.util import check_if_table_exists, check_if_column_exists

column_info = {
    'field': 0,
    'type': 1,
    'null': 2,
    'key': 3,
    'default': 4,
    'extra': 5
}


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

    print(columns)
    return columns


def compare_tables():
    tables_plan = []
    connection = Engine.get_connection()

    json_file = open(configs_dir + '/' + file_name)
    data = json.load(json_file)

    for table in data:
        if check_if_table_exists(connection, table['name']) is False:
            plan = {
                table['name']: 'Create'
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
                    table['name']: 'Update'
                }
                tables_plan.append(plan)
            else:
                plan = {
                    table['name']: 'Existed'
                }
                tables_plan.append(plan)

    return tables_plan
