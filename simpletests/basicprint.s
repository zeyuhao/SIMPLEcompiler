.data

.balign 4
b: .word 0

.text
.global main

main:
	push {lr}

	ldr r0, =wformat
	mov r1, #47
	bl printf

end:
	pop {pc}

wformat: .asciz "%d\n"
addr_b : .word b
