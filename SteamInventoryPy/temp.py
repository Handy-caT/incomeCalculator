from sqlalchemy import text

from db import *
from db.engine import Engine
from migrations.util import make_dir, save_table_state

# make_dir()
# save_table_state('users', 'v1', 'existed')
from sql.exceptions import IncorrectColumnType
from sql.table_scan import scan_tables, save_json

# print(scan_tables())
# save_json(scan_tables())
from sql.update_plan import compare_tables, save_plan, check_index
from sql.util import get_indexes

connection = Engine.get_connection()
# print(check_index(connection, 'users', 'is_admin'))

plan = compare_tables()
print(plan)

# save_plan(plan, 'v1')

# raise IncorrectColumnType('users', 'INT', 'BIGINT')
