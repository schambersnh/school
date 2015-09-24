#set terminal pdf
#set output "latency-3D.pdf"
set terminal wxt

set title "Throughput (MB/s)"
set xlabel "Packet loss rate\n(%)"
set ylabel "Network latency\n(ms)"
#set logscale z

set palette gray
unset colorbox
set view 60,120
set hidden3d front
set pm3d implicit at s

splot [0:10][0:40]\
	"http-3d.txt" using 1:2:($5/1000000.0) with lines lc rgb "green" lw 3title "HTTP",\
	"spdy-3d.txt" using 1:2:($5/1000000.0) with lines lc rgb "magenta" lw 3 title "SPDY" 

#waits for keystroke, obviously remove if in script
pause -1
