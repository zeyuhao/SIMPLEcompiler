.data

.balign 4
arr: .skip 20

.balign 4
index: .word 0

.text
.global main

main:
	push {lr}

	ldr r0, =rformat
	ldr r2, addr_index
	add r2, r2, #0
	mov r1, r2
	bl scanf

	mov r4, #4
	ldr r3, addr_index
	ldr r3, [r3]
	mov r5, #5
	cmp r3, r5
	blt continue0
	ldr r0, =eformat0
	bl printf
	b end
continue0:
	mul r3, r3, r4
	mov r2, r3
	ldr r0, addr_arr
	push {r0}
	mov r1, #50
	pop {r0}
	str r1, [r0, +r2]

	ldr r0, =wformat
	mov r4, #4
	ldr r3, addr_index
	ldr r3, [r3]
	mov r5, #5
	cmp r3, r5
	blt continue1
	ldr r0, =eformat1
	bl printf
	b end
continue1:
	mul r3, r3, r4
	mov r2, r3
	ldr r1, addr_arr
	ldr r1, [r1, +r2]
	bl printf

end:
	pop {pc}

wformat: .asciz "%d\n"
rformat: .asciz "%d"

eformat0: .asciz "Index identifier<index>@(96, 100) is out of bounds. Size of Array arr is 5\n"
eformat1: .asciz "Index identifier<index>@(122, 126) is out of bounds. Size of Array arr is 5\n"

addr_arr: .word arr
addr_index : .word index
