package com.darkyen.minecraft.utils.channels

import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel

/**
 *
 */
class ByteBufferChannel(private val buffer: ByteBuffer) : SeekableByteChannel {

    @JvmOverloads
    constructor(size: Int = 8096) : this(ByteBuffer.allocate(size))

    init {
        buffer.limit(0)
    }

    override fun read(dst: ByteBuffer): Int {
        val available = buffer.remaining()
        if (available == 0) {
            return -1
        }
        val toRead = dst.remaining()
        if (available > toRead) {
            val oldLimit = buffer.limit()
            buffer.limit(buffer.position() + toRead)
            dst.put(buffer)
            buffer.limit(oldLimit)
            return toRead
        } else {
            // available < toRead
            dst.put(buffer)
            return available
        }
    }

    override fun write(src: ByteBuffer): Int {
        val toWrite = src.remaining()
        if (buffer.remaining() < toWrite) {
            // Expand
            buffer.limit(buffer.position() + toWrite)
        }
        buffer.put(src)
        return toWrite
    }

    override fun position(): Long {
        return buffer.position().toLong()
    }

    override fun position(newPosition: Long): ByteBufferChannel {
        buffer.position(newPosition.toInt())
        return this
    }

    override fun size(): Long {
        return buffer.limit().toLong()
    }

    override fun truncate(size: Long): ByteBufferChannel {
        buffer.limit(size.toInt())
        return this
    }

    override fun isOpen(): Boolean {
        return true
    }

    override fun close() = Unit
}
