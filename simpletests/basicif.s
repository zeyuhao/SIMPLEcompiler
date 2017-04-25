.data

.balign 4
b: .word 0

.balign 4
c: .word 0

.balign 4
d: .skip 8836

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

start_if0:
	ldr r0, addr_b
	ldr r0, [r0, +#0]
	ldr r1, addr_c
	ldr r1, [r1, +#0]
	cmp r0, r1
	bne else1
if0:
	ldr r0, addr_b
	push {r0}
	mov r1, #100
	pop {r0}
	str r1, [r0, +#0]

	b end_if0
else1:
	ldr r0, addr_b
	push {r0}
	mov r1, #0
	pop {r0}
	str r1, [r0, +#0]
end_if0:

	ldr r0, =wformat
	ldr r1, addr_b
	ldr r1, [r1, +#0]
	bl printf

	ldr r3, =#8648
	mov r2, r3
	mov r3, #184
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	ldr r0, addr_d
	push {r0}
	mov r1, #47
	pop {r0}
	str r1, [r0, +r2]

	ldr r0, =wformat
	ldr r3, =#8648
	mov r2, r3
	mov r3, #184
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	ldr r1, addr_d
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, =wformat
	ldr r3, =#8648
	mov r2, r3
	mov r3, #180
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	ldr r1, addr_d
	ldr r1, [r1, +r2]
	bl printf

end:
	pop {pc}

wformat: .asciz "%d\n"
rformat: .asciz "%d"

addr_b : .word b
addr_c : .word c
addr_d: .word d
