# Database

This project uses the **room persistence library** to provide an abstraction layer over SQLite to allow fluent database access.
For more information, see: https://developer.android.com/training/data-storage/room#groovy


There are three major components in Room:

- The **database class** that holds the database and serves as the main access point for the underlying connection to the app's persisted data.
- **Data entities** that represent tables in the app's database.
- **Data access objects** (DAOs) that provide methods that the app can use to query, update, insert, and delete data in the database.
