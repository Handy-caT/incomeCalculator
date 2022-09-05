
column_info = {
    'field': 0,
    'type': 1,
    'null': 2,
    'key': 3,
    'default': 4,
    'extra': 5
}


def check_nullable(column):
    if column[column_info['null']] == 'NO':
        return False
    else:
        return True


def check_primary_key(column):
    if column[column_info['key']] == 'PRI':
        return True
    else:
        return False


def check_unique(column):
    if column[column_info['key']] == 'UNI':
        return True
    else:
        return False


def check_auto_increment(column):
    if column[column_info['extra']] == 'auto_increment':
        return True
    else:
        return False


def check_default(column, default_value):
    if column[column_info['default']] == default_value:
        return True
    else:
        return False


def check_type(column, type_name):
    if column[column_info['type']].lower().find('datetime') != -1 and type_name.lower().find('datetime') != -1:
        return True

    if column[column_info['type']].lower().find('bit') != -1 and type_name.lower().find('boolean') != -1:
        return True

    if column[column_info['type']].lower() == type_name.lower():
        return True
    else:
        return False
