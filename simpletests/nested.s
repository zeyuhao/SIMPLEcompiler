.data

.balign 4
a: .word 0

.balign 4
z: .word 0

.text
.global main

main:
	push {lr}

	ldr r0, =rformat
	ldr r2, addr_a
	add r2, r2, #0
	mov r1, r2
	bl scanf

	ldr r0, addr_z
	push {r0}
	ldr r1, addr_a
	ldr r1, [r1, +#0]
	add r1, r1, #1
	push {r1}
	ldr r2, addr_a
	ldr r2, [r2, +#0]
	add r2, r2, #1
	push {r2}
	ldr r2, addr_a
	ldr r2, [r2, +#0]
	pop {r2}
	add r2, r2, #1
	push {r2}
	ldr r2, addr_a
	ldr r2, [r2, +#0]
	pop {r2}
	add r2, r2, #1
	push {r2}
	ldr r2, addr_a
	ldr r2, [r2, +#0]
	pop {r2}
	add r2, r2, #1
	push {r2}
	ldr r2, addr_a
	ldr r2, [r2, +#0]
	pop {r2}
	add r2, r2, #1
	push {r2}
	ldr r2, addr_a
	ldr r2, [r2, +#0]
	pop {r2}
	add r2, r2, #1
	push {r2}
	ldr r2, addr_a
	ldr r2, [r2, +#0]
	pop {r2}
	add r2, r2, #1
	push {r2}
	ldr r2, addr_a
	ldr r2, [r2, +#0]
	pop {r2}
	add r2, r2, #1
	push {r2}
	ldr r2, addr_a
	ldr r2, [r2, +#0]
	pop {r2}
	add r2, r2, #1
	push {r2}
	pop {r2}
	add r2, r2, r2
	push {r2}
	pop {r2}
	add r2, r2, r2
	push {r2}
	pop {r2}
	add r2, r2, r2
	push {r2}
	pop {r2}
	add r2, r2, r2
	push {r2}
	pop {r2}
	add r2, r2, r2
	push {r2}
	pop {r2}
	add r2, r2, r2
	push {r2}
	pop {r2}
	add r2, r2, r2
	push {r2}
	pop {r2}
	add r2, r2, r2
	push {r2}
	pop {r2}
	pop {r1}
	add r1, r1, r2
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, =wformat
	ldr r1, addr_z
	ldr r1, [r1, +#0]
	bl printf

end:
	pop {pc}

wformat: .asciz "%d\n"
rformat: .asciz "%d"

addr_a : .word a
addr_z : .word z
