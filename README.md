# Obscure's Checklist
This utility is designed to allow me to easily keep a to-do list of both personal tasks and assignments with date/time deadlines.

I am planning on adding support for changing the value and type of any task, as well as storing and reading descriptions of tasks. I'll probably also write a better Readme, and maybe even a how-to-use guide after I add more features, which I'm doing when I can find time, when they occur to me, or when I find a use for them.

Here's all I'll explain for now:\
This program can store, display, and sort three different kinds of tasks:
1. Date tasks
    - These tasks have an explicit deadline, specified with Year, Month, Day, Hour, and Minute. Most often used for assignments.
1. Score tasks
    - These tasks don't have an explicit deadline, but are instead given an integer "priority score" based on how urgent you think task is. Useful for hobby tasks or personal to-dos with no due date. Lower scores are more urgent. 0 or lower is extremely urgent, 1 to 5 are moderately urgent, and 6 or greater aren't urgent.
1. Null-type tasks
    - These tasks have no deadline and no priority score. They will always be at the very bottom of the list. Useful for tasks that you just want to jot down, and might assign a score or date to later, or tasks that you really just don't care about.

Here's the logic behind the comparison sorting of tasks:
1. If both objects are Null-type, then compare titles alphabetically.
1. If one object is Null-type and the other isn't, the Null-type always loses.
1. If both objects are Date types:
    1. If the dates match, compare titles alphabetically.
    1. If the dates mismatch, earlier dates win.
1. If both objects are Score types:
    1. If scores match, compare titles alphabetically.
    1. If scores mismatch, lower scores win.
1. If one object is a Date type and the other is a Score type:
    1. If the Date type is overdue, Date type wins.
    1. If that didn't happen, and the Score type is less than or equal to 0, Score type wins.
    1. If that didn't happen, and Date type is 48 or fewer hours from now, Date type wins.
    1. If that didn't happen, and Score type is less than or equal to 5, Score type wins.
    1. If that didn't happen, Date type wins.

I use a ternary-state boolean trick in Java to store the type, just for fun. You see, the `boolean` primitive in Java can be `true` or `false`, but the `Boolean` *class* in Java can be `true` or `false` or `null`.