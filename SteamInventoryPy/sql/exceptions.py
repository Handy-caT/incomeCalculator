
class IncorrectColumnType(Exception):

    def __init__(self, column_name, type_db, type_python):
        self.column_name = column_name
        self.type_db = type_db
        self.type_python = type_python
        super().__init__(f'Incorrect column type in {self.column_name} column. Type in db: {self.type_db}, '
                         f'type in python: {self.type_python}')


class IncorrectPrimaryKey(Exception):

    def __init__(self, column_name):
        self.column_name = column_name
        super().__init__(f'Incorrect primary key in {self.column_name} column. Primary key must be named id')
