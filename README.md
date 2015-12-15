Firekraken
=========

---

Introduction
---------

Welcome to this short overview of Firekraken.


Interactive
---------

To run the code discussed in this tutorial
Some time has passed since if first wrote
this.  It needs a few dated components to
run.

- download : java 6
- download : maven 2
- Run maven install from the console
  > mvn install
- Run the console application
  > mvn exec:java
- It should return a prompt

Legend
---------

For this tutorial the code will appear as follows

    !sql
    SELECT 'hello world!!' FROM identity

The resulting output will appear as follows

> _hello world_


---

Motivation
========

The motivation for firekraken came out of the desire to write
a high performance stream database.  As there were no high-volume
low-latency data streams publicly available; a database was
designed around a data stream of function calls.

This work resulted in the firekraken.

---

Create Table
========

Java:

    !java
    @Table("identity")
    public static Object identity(@Column("x")Object x){
    return x;
    }

Consider this function implementing the identity operator in java.
The table annotation _@Table("identity")_ instructs firekraken
to transform identity into a "table".  The annotation is analogous
to issuing the following _create table_ sql command.

SQL:

    !sql
    CREATE TABLE identity
    (x Object
    ,this Object
    ,result Object)

When identity is called a new record will created and operated on by the
running sql commands.

---

Statements
========

- CALL
- SELECT
- INSERT
- UPDATE
- DELETE

Given we have a database table lets explore what it means to select, insert,
update and delete on this table.

---

Call
========

The following line of java code.

    !java
    print("hello world")

is is equavalent to the following statement.

    !sql
    CALL print("hello world")

> _hello world_

> _null_

This convenience statement CALL evaluates
a given expression. This expression prints the statement :

> _"hello world"_


The _null_ is java's representation of the unit result of the
print operator.

---

Select
========


Stream databases query over time instead of space, as a result,
selects statements in firekraken are nonterminating. The queries
run in the background and return rows matching the criteria specified
when matches are found.

    !sql
    select "identity was called with value : ", x from identity

This is query without any criteria on the table identity.  This
query against this table will return a row when the function identity
is called.

    !sql
    CALL identity(42)

> _42_

> _[identity was called with value : , 42]_

When a call statement on identity is issued we see the result of our query 42.
We also get the row as a result to our running the  select statement.

---

Insert
========

    !sql
    insert into identity (x) values (496)

> _496_

> _[identity was called with value : , 496]_

Here we see 496 the result of inserting into identity as inserting into
this table is invoking the function with values of the row the being the
function argument.

This also means that the query will return another result row for 496.

---

Update
========


    !sql
    update identity set x = modulo2(x)

By updating this function we can alter the arguments passed to the function
and the return value of the function.
Here the parameters to identity are changed modulo 2.

    !sql
    CALL identity(1)

> _1_

> _[identity was called with value : , 1]_

    !sql
    CALL identity(2)

> _0_

> _[identity was called with value : , 0]_


Update "updates" the parameters (and results) of the function.
This behavior holds not just for calls but for function calls
within the running program.

---

Delete
========


    !sql
    delete from identity where x = 1

We can also delete from the this table.
This means that when the criteria is matched
the function will not be evaluated.

    !sql
    CALL identity(10)

> _0_

> _[identity was called with value : , 0]_

The previous update sets the value to 0 and the
delete does not affect the evaluation of identity.

    !sql
    CALL identity(11)

> _null_

The previous update sets the value to 1 (11 modulo 2)
and the delete prevents the identity from being called
hence we get a _null_.

---

Conclusion
========

Ponder
--------

The call statement is an convince statement.  Is there a
SQL statement that can be used in place of the CALL statement?

Explore
--------

Expolore the demo jar and experiment with the following

- tables : fibonacci(x),identity(x),add(x,y)

- functions : fibonacci(x),quit(),identity(x),print(x),add(x,y)
