.text
.align 4
.global asm_yield
.global asm_special_yield

asm_yield:

# What all good Intel functions do

###################################################
pushl %ebp # push frame pointer on top of the stack
movl %esp, %ebp # move frame pointer up
###################################################

#save current thread's state in it's TCB

###################################################
movl 8(%ebp), %eax # get cur argument

movl %esp, (%eax) # put esp into cur's TCB
movl %edi, 4(%eax) # put edi into cur's TCB
movl %esi, 8(%eax) # put edi into cur's TCB 
movl %ebx, 12(%eax) # put edi into cur's TCB 
###################################################

#restore next threads state from it's TCB

###################################################
movl 12(%ebp), %eax # get next argument

movl (%eax), %esp #restore next thread's esp
movl 4(%eax), %edi #restore next thread's edi
movl 8(%eax), %esi #restore next thread's esi
movl 12(%eax), %ebx #restore next thread's ebx
###################################################


#How all good Intel functions end

popl %ebp
movl $0, critFlag
ret

asm_special_yield:

# What all good Intel functions do

###################################################
pushl %ebp # push frame pointer on top of the stack
movl %esp, %ebp # move frame pointer up
###################################################

# don't save current thread, it is committing suicide

#restore next threads state from it's TCB

###################################################
movl 8(%ebp), %eax # get next argument

movl (%eax), %esp #restore next thread's esp
movl 4(%eax), %edi #restore next thread's edi
movl 8(%eax), %esi #restore next thread's esi
movl 12(%eax), %ebx #restore next thread's ebx
###################################################

#How all good Intel functions end

popl %ebp
movl $0, critFlag
ret

