.data

.balign 4
a: .word 0

.balign 4
b: .word 0

.balign 4
c: .word 0

.text
.global main

main:
	push {lr}

	ldr r0, =rformat
	ldr r2, addr_b
	add r2, r2, #0
	mov r1, r2
	bl scanf

	ldr r0, =rformat
	ldr r2, addr_c
	add r2, r2, #0
	mov r1, r2
	bl scanf

	ldr r0, addr_a
	push {r0}
	ldr r1, addr_b
	ldr r1, [r1, +#0]
	ldr r2, addr_c
	ldr r2, [r2, +#0]
	mov r0, r1
	mov r1, r2
	cmp r1, #0
	bne continue0
	ldr r0, =divbyzeroformat0
	bl printf
	pop {r0}
	b end
continue0:
	bl __aeabi_idivmod
	mov r1, r0
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, =wformat
	ldr r1, addr_a
	ldr r1, [r1, +#0]
	bl printf

end:
	pop {pc}

wformat: .asciz "%d\n"
rformat: .asciz "%d"

divbyzeroformat0: .asciz "DIV by zero found in Expression: b DIV c@(105, 105)\n"

addr_a : .word a
addr_b : .word b
addr_c : .word c
