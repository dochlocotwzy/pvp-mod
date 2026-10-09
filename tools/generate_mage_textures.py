from pathlib import Path
import struct,zlib
R=Path(__file__).resolve().parents[1]
S={"magic_barrier.png":"tools/textures/magic_barrier.png.hex","flash_of_light.png":"tools/textures/flash_of_light.png.hex","energy_impulse.png":"tools/textures/shockwave.txt"}
def ck(t,d):return struct.pack(">I",len(d))+t+d+struct.pack(">I",zlib.crc32(t+d)&0xffffffff)
for n,s in S.items():
 l=[x.strip() for x in (R/s).read_text().splitlines() if x.strip()];p=[int(x,16) for x in l[0].split(",")];rows=l[1:]
 if len(rows)!=16 or any(len(x)!=16 for x in rows):raise ValueError(s)
 raw=bytearray()
 for row in rows:
  raw.append(0)
  for ch in row:
   c=p[int(ch,16)];raw.extend(((c>>16)&255,(c>>8)&255,c&255,(c>>24)&255))
 data=bytes([137,80,78,71,13,10,26,10])+ck(b"IHDR",struct.pack(">IIBBBBB",16,16,8,6,0,0,0))+ck(b"IDAT",zlib.compress(raw))+ck(b"IEND",b"")
 out=R/"src/main/resources/assets/pvptraps/textures/item"/n;out.parent.mkdir(parents=True,exist_ok=True);out.write_bytes(data)
