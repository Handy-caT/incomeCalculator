import json
from pathlib import Path

from db.engine import Engine
from db import *

configs_dir = 'migrations/config'


def make_dir():
    Path(configs_dir).mkdir(parents=True, exist_ok=True)


def save_json(tables):
    make_dir()

    json_str = json.dumps(tables, indent=3)
    with open(configs_dir + '/tables.json', 'w') as f:
        f.write(json_str)


def scan_tables():
    metadata = Engine.get_metadata()
    tables = []

    for table in metadata.sorted_tables:
        columns = {}

        for column in table.columns:
            columns[column.name] = {
                'type': str(column.type),
                'nullable': column.nullable,
                'primary_key': column.primary_key,
                'unique': column.unique,
                'index': column.index,
                'default': column.default,
            }

        tables.append({
            'name': table.name.lower(),
            'columns': columns,
        })

    return tables
