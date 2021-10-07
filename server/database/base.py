import uuid
from typing import TYPE_CHECKING, Any, no_type_check

from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import as_declarative, declared_attr
from sqlalchemy.types import CHAR, TypeDecorator

if TYPE_CHECKING:
    UUIDTypeDecorator = TypeDecorator[uuid.UUID]
else:
    UUIDTypeDecorator = TypeDecorator


# https://docs.sqlalchemy.org/en/14/core/custom_types.html#backend-agnostic-guid-type
class GUID(UUIDTypeDecorator):  # type: ignore
    """Platform-independent UUID type.

    Uses PostgreSQL's UUID type, otherwise uses
    CHAR(32), storing as stringified hex values.

    """

    impl = CHAR
    cache_ok = True

    @no_type_check
    def load_dialect_impl(self, dialect):
        if dialect.name == "postgresql":
            return dialect.type_descriptor(UUID())
        else:
            return dialect.type_descriptor(CHAR(32))

    @no_type_check
    def process_bind_param(self, value, dialect):
        if value is None:
            return value
        elif dialect.name == "postgresql":
            return str(value)
        else:
            if not isinstance(value, uuid.UUID):
                return "%.32x" % uuid.UUID(value).int
            else:
                # hexstring
                return "%.32x" % value.int

    @no_type_check
    def process_result_value(self, value, dialect):
        if value is None:
            return value
        else:
            if not isinstance(value, uuid.UUID):
                value = uuid.UUID(value)
            return value


@as_declarative()
class Base:
    __name__: str
    id: Any

    @declared_attr  # type: ignore
    def __tablename__(cls):
        return cls.__name__.lower()
