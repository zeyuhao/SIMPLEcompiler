.data

.balign 4
v: .skip 20

.balign 4
z: .skip 20

.text
.global main

main:
	push {lr}

	mov r3, #0
	mov r2, r3
	ldr r0, addr_z
	push {r0}
	mov r1, #5
	pop {r0}
	str r1, [r0, +r2]

	mov r3, #4
	mov r2, r3
	ldr r0, addr_z
	push {r0}
	mov r1, #5
	pop {r0}
	str r1, [r0, +r2]

	ldr r0, =wformat
	mov r3, #0
	mov r2, r3
	ldr r1, addr_z
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, =wformat
	mov r3, #4
	mov r2, r3
	ldr r1, addr_z
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, addr_v
	ldr r1, addr_z
	mov r2, #0
	mov r4, #4
copy_start0:
	cmp r2, #5
	beq copy_end0
	mul r3, r2, r4
	ldr r5, [r1, +r3]
	str r5, [r0, +r3]
	add r2, r2, #1
	b copy_start0
copy_end0:

	ldr r0, =wformat
	mov r3, #0
	mov r2, r3
	ldr r1, addr_v
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, =wformat
	mov r3, #4
	mov r2, r3
	ldr r1, addr_v
	ldr r1, [r1, +r2]
	bl printf

	mov r3, #4
	mov r2, r3
	ldr r0, addr_v
	push {r0}
	mov r1, #10
	pop {r0}
	str r1, [r0, +r2]

	ldr r0, =wformat
	mov r3, #4
	mov r2, r3
	ldr r1, addr_v
	ldr r1, [r1, +r2]
	bl printf

end:
	pop {pc}

wformat: .asciz "%d\n"
addr_v: .word v
addr_z: .word z
