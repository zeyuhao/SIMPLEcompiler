.data

.balign 4
Z: .word 0

.balign 4
g: .word 0

.balign 4
ind: .word 0

.balign 4
q: .word 0

.text
.global main

main:
	push {lr}

	ldr r0, =rformat
	ldr r2, addr_Z
	add r2, r2, #0
	mov r1, r2
	bl scanf

	ldr r0, =rformat
	ldr r2, addr_q
	add r2, r2, #0
	mov r1, r2
	bl scanf

start_if0:
	ldr r0, addr_ind
	ldr r0, [r0, +#0]
	mov r1, #5
	cmp r0, r1
	bge end_if0

if0:
rep1:
	ldr r0, addr_g
	push {r0}
	ldr r2, addr_Z
	ldr r2, [r2, +#0]
	ldr r3, addr_q
	ldr r3, [r3, +#0]
	mov r0, r2
	mov r1, r3
	cmp r1, #0
	bne continue0
	ldr r0, =modbyzeroformat0
	bl printf
	pop {r0}
	b end
continue0:
	bl __aeabi_idivmod
	mov r2, r1
	push {r2}
	mov r1, #50
	pop {r2}
	mul r1, r1, r2
	push {r1}
	ldr r3, addr_Z
	ldr r3, [r3, +#0]
	ldr r4, addr_q
	ldr r4, [r4, +#0]
	mov r0, r3
	mov r1, r4
	cmp r1, #0
	bne continue1
	ldr r0, =divbyzeroformat1
	bl printf
	pop {r0}
	pop {r1}
	b end
continue1:
	bl __aeabi_idivmod
	mov r3, r0
	push {r3}
	mov r2, #1
	pop {r3}
	mul r2, r2, r3
	push {r2}
	pop {r2}
	pop {r1}
	sub r1, r1, r2
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

start_if2:
	ldr r0, addr_g
	ldr r0, [r0, +#0]
	mov r1, #0
	cmp r0, r1
	ble else3
if2:
	ldr r0, addr_Z
	push {r0}
	ldr r1, addr_g
	ldr r1, [r1, +#0]
	pop {r0}
	str r1, [r0, +#0]

	b end_if2
else3:
	ldr r0, addr_Z
	push {r0}
	mov r1, #200
	pop {r0}
	str r1, [r0, +#0]
end_if2:

	ldr r0, =wformat
	ldr r1, addr_Z
	ldr r1, [r1, +#0]
	bl printf

	ldr r0, addr_ind
	push {r0}
	ldr r1, addr_ind
	ldr r1, [r1, +#0]
	add r1, r1, #1
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, addr_ind
	ldr r0, [r0, +#0]
	mov r1, #5
	cmp r0, r1
	blt rep1
	b end_if0
end_if0:

end:
	pop {pc}

wformat: .asciz "%d\n"
rformat: .asciz "%d"

modbyzeroformat0: .asciz "DIV by zero found in Expression: Z MOD q@(260, 260)\n"
divbyzeroformat1: .asciz "DIV by zero found in Expression: Z DIV q@(274, 274)\n"

addr_Z : .word Z
addr_g : .word g
addr_ind : .word ind
addr_q : .word q
