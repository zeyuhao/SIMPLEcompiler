.data

.balign 4
i: .word 0

.text
.global main

main:
	push {lr}

rep0:
	ldr r0, =wformat
	ldr r1, addr_i
	ldr r1, [r1, +#0]
	bl printf

	ldr r0, addr_i
	push {r0}
	ldr r1, addr_i
	ldr r1, [r1, +#0]
	add r1, r1, #1
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #5
	cmp r0, r1
	blt rep0
end:
	pop {pc}

wformat: .asciz "%d\n"
addr_i : .word i
