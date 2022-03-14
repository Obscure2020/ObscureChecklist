# Obscure's Checklist
This utility is designed to allow me to easily keep a to-do list of both personal tasks and assignments with date/time deadlines.

If you'd like to use it for yourself, do so at your own risk. This program is ***VERY*** incomplete at this time. For crying out loud, I didn't even tell the program what to do if `Book.dat` isn't present.

I am planning on adding support for changing the value and type of any task, as well as storing and reading descriptions of tasks.

I'll probably also write a better Readme, and maybe even a how-to-use guide after I add more features. What's present so far is enough for me to start testing it out in daily use. I'll add more features when I can find time, when they occur to me, or when I find a use for them.

Here's all I'll explain for now:\
This program can store, display, and sort three different kinds of tasks:
1. Date tasks
    - These tasks have an explicit deadline, specified with Year, Month, Day, Hour, and Minute. Most often used for assignments.
1. Score tasks
    - These tasks don't have an explicit deadline, but are instead given an integer "priority score" based on how urgent you think task is. Useful for hobby tasks or personal to-dos with no due date.
        - A lower score is more urgent.
        - 0 or lower is incredibly urgent, gets moved to the top of the list.
        - 1 through 5 are moderately urgent and get put near the top of the list, but will be beat out by imminiently-due Date tasks.
        - 6 or greater are not urgent and will be beat out by any Date task.
            - (Perhaps I should change this so that they beat out Date tasks that are a certain number of months away?)
1. Null-type tasks
    - These tasks have no deadline and no priority score. They will always be at the very bottom of the list. Useful for tasks that you just want to jot down, and might assign a score or date to later, or tasks that you really just don't care about.

I use a ternary-state boolean trick in Java to store the type, just for fun. You see, the `boolean` primitive in Java can be `true` or `false`, but the `Boolean` *class* in Java can be `true` or `false` or `null`.