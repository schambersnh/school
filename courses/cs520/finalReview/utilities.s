#1. Write add 11 in assembly, takes an int, adds 11 to it, and returns the result
#2. Write threadYield

.text
.align 4
.globl asm_add11
.globl asm_yield

asm_add11:
pushl %ebp
movl %esp, %ebp
###############################################################################
movl 8(%ebp), %eax
addl $11, %eax
###############################################################################
popl %ebp
ret

asm_yield:
pushl %ebp
movl %esp, %ebp

movl 8(%ebp), %eax
movl %esp, (%eax)
movl %esi, 4(%eax)
movl %edi, 8(%eax)
movl %ebx, 12(%eax)

movl 12(%ebp), %eax

movl (%eax), %esp
movl 4(%eax), %esi
movl 8(%eax), %edi
movl 12(%eax), %ebx

popl %ebp
ret 
