'use strict';

function appendByteArray(buffer1, buffer2) {
    let tmp = new Uint8Array((buffer1.byteLength | 0) + (buffer2.byteLength | 0));
    tmp.set(buffer1, 0);
    tmp.set(buffer2, buffer1.byteLength | 0);
    return tmp;
}

function base64ToArrayBuffer(base64) {
    var spropParameterSetDecode = window.atob(base64);
    var length = spropParameterSetDecode.length;
    var data = new Uint8Array(length);
    var i = 0;
    for (; i < length; i++) {
        data[i] = spropParameterSetDecode.charCodeAt(i);
    }
    return data.buffer;
}

function u8ToBase64(utf8String) {
    return btoa(String.fromCharCode.apply(null, utf8String));
}

function hexToByteArray(hex) {
    let len = hex.length >> 1;
    var bufView = new Uint8Array(len);
    var i = 0;
    for (; i < len; i++) {
        bufView[i] = parseInt(hex.substr(i << 1, 2), 16);
    }
    return bufView;
}

function bitSlice(bytearray, start = 0, end = bytearray.byteLength * 8) {
    let byteLen = Math.ceil((end - start) / 8);
    let res = new Uint8Array(byteLen);
    let startByte = start / 8 >> 0;
    let endByte = (end / 8 >> 0) - 1;
    let bitOffset = start % 8;
    let nBitOffset = 8 - bitOffset;
    let endOffset = 8 - end % 8;
    for (let i = 0; i < byteLen; ++i) {
        let tail = 0;
        if (i < endByte) {
            tail = bytearray[startByte + i + 1] >> nBitOffset;
            if (i == endByte - 1 && endOffset < 8) {
                tail = tail >> endOffset;
                tail = tail << endOffset;
            }
        }
        res[i] = bytearray[startByte + i] << bitOffset | tail;
    }
    return res;
}

class BitArray {
    constructor(src) {
        this.src = new DataView(src.buffer, src.byteOffset);
        this.bitpos = 0;
        this.byte = 0;
        this.bytepos = 0;
    }

    readBits(length) {
        if (32 < (length | 0) || 0 === (length | 0)) {
            throw new Error('too big');
        }
        let result = 0;
        this.byte = this.src.getUint8(this.bytepos);
        for (let i = 1; i <= length; ++i) {
            result = (result | 0) << 1 | (this.byte | 0) >> 8 - ++this.bitpos & 1;
            if ((this.bitpos | 0) >= 8) {
                this.byte = this.src.getUint8(++this.bytepos);
            }
            this.bitpos %= 8;
        }
        return result;
    }

    skipBits(length) {
        this.bitpos += (length | 0) % 8;
        this.bytepos += (length | 0) / 8 >> 0;
        this.byte = this.src.getUint8(this.bytepos);
    }
}

class ExpGolomb {
    constructor(data) {
        this.data = data;
        // the number of bytes left to examine in this.data
        this.bytesAvailable = data.byteLength;
        // the current word being examined
        this.word = 0; // :uint
        // the number of bits left to examine in the current word
        this.bitsAvailable = 0; // :uint
    }

    // ():void
    loadWord() {
        let
            data = this.data,
            bytesAvailable = this.bytesAvailable,
            position = data.byteLength - bytesAvailable,
            workingBytes = new Uint8Array(4),
            availableBytes = Math.min(4, bytesAvailable);
        if (availableBytes === 0) {
            throw new Error('no bytes available');
        }

        workingBytes.set(data.subarray(position, position + availableBytes));
        this.word = new DataView(workingBytes.buffer).getUint32(0);
        // track the amount of this.data that has been processed
        this.bitsAvailable = availableBytes * 8;
        this.bytesAvailable -= availableBytes;
    }

    // (count:int):void
    skipBits(count) {
        let skipBytes; // :int
        if (this.bitsAvailable > count) {
            this.word <<= count;
            this.bitsAvailable -= count;
        } else {
            count -= this.bitsAvailable;
            skipBytes = count >> 3;
            count -= (skipBytes >> 3);
            this.bytesAvailable -= skipBytes;
            this.loadWord();
            this.word <<= count;
            this.bitsAvailable -= count;
        }
    }

    // (size:int):uint
    readBits(size) {
        let
            bits = Math.min(this.bitsAvailable, size), // :uint
            valu = this.word >>> (32 - bits); // :uint
        if (size > 32) {
            logger.error('Cannot read more than 32 bits at a time');
        }

        this.bitsAvailable -= bits;
        if (this.bitsAvailable > 0) {
            this.word <<= bits;
        } else if (this.bytesAvailable > 0) {
            this.loadWord();
        }

        bits = size - bits;
        if (bits > 0 && this.bitsAvailable) {
            return valu << bits | this.readBits(bits);
        } else {
            return valu;
        }
    }

    // ():uint
    skipLZ() {
        let leadingZeroCount; // :uint
        for (leadingZeroCount = 0; leadingZeroCount < this.bitsAvailable; ++leadingZeroCount) {
            if ((this.word & (0x80000000 >>> leadingZeroCount)) !== 0) {
                // the first bit of working word is 1
                this.word <<= leadingZeroCount;
                this.bitsAvailable -= leadingZeroCount;
                return leadingZeroCount;
            }
        }
        // we exhausted word and still have not found a 1
        this.loadWord();
        return leadingZeroCount + this.skipLZ();
    }

    // ():void
    skipUEG() {
        this.skipBits(1 + this.skipLZ());
    }

    // ():void
    skipEG() {
        this.skipBits(1 + this.skipLZ());
    }

    // ():uint
    readUEG() {
        let clz = this.skipLZ(); // :uint
        return this.readBits(clz + 1) - 1;
    }

    // ():int
    readEG() {
        let valu = this.readUEG(); // :int
        if (0x01 & valu) {
            // the number is odd if the low order bit is set
            return (1 + valu) >>> 1; // add 1 to make it even, and divide by 2
        } else {
            return -1 * (valu >>> 1); // divide by two then make it negative
        }
    }

    // Some convenience functions
    // :Boolean
    readBoolean() {
        return this.readBits(1) === 1;
    }

    // ():int
    readUByte() {
        return this.readBits(8);
    }

    // ():int
    readUShort() {
        return this.readBits(16);
    }

    // ():int
    readUInt() {
        return this.readBits(32);
    }

    /**
     * Advance the ExpGolomb decoder past a scaling list. The scaling
     * list is optionally transmitted as part of a sequence parameter
     * set and is not relevant to transmuxing.
     * @param count {number} the number of entries in this scaling list
     * @see Recommendation ITU-T H.264, Section 7.3.2.1.1.1
     */
    skipScalingList(count) {
        let
            lastScale = 8,
            nextScale = 8,
            j,
            deltaScale;
        for (j = 0; j < count; j++) {
            if (nextScale !== 0) {
                deltaScale = this.readEG();
                nextScale = (lastScale + deltaScale + 256) % 256;
            }
            lastScale = (nextScale === 0) ? lastScale : nextScale;
        }
    }

    /**
     * Read a sequence parameter set and return some interesting video
     * properties. A sequence parameter set is the H264 metadata that
     * describes the properties of upcoming video frames.
     * @param data {Uint8Array} the bytes of a sequence parameter set
     * @return {object} an object with configuration parsed from the
     * sequence parameter set, including the dimensions of the
     * associated video frames.
     */
    readSPS() {
        let
            frameCropLeftOffset = 0,
            frameCropRightOffset = 0,
            frameCropTopOffset = 0,
            frameCropBottomOffset = 0,
            profileIdc, profileCompat, levelIdc,
            numRefFramesInPicOrderCntCycle, picWidthInMbsMinus1,
            picHeightInMapUnitsMinus1,
            frameMbsOnlyFlag,
            scalingListCount,
            i,
            readUByte = this.readUByte.bind(this),
            readBits = this.readBits.bind(this),
            readUEG = this.readUEG.bind(this),
            readBoolean = this.readBoolean.bind(this),
            skipBits = this.skipBits.bind(this),
            skipEG = this.skipEG.bind(this),
            skipUEG = this.skipUEG.bind(this),
            skipScalingList = this.skipScalingList.bind(this);

        readUByte();
        profileIdc = readUByte(); // profile_idc
        profileCompat = readBits(5); // constraint_set[0-4]_flag, u(5)
        skipBits(3); // reserved_zero_3bits u(3),
        levelIdc = readUByte(); // level_idc u(8)
        skipUEG(); // seq_parameter_set_id
        // some profiles have more optional data we don't need
        if (profileIdc === 100 ||
            profileIdc === 110 ||
            profileIdc === 122 ||
            profileIdc === 244 ||
            profileIdc === 44 ||
            profileIdc === 83 ||
            profileIdc === 86 ||
            profileIdc === 118 ||
            profileIdc === 128) {
            let chromaFormatIdc = readUEG();
            if (chromaFormatIdc === 3) {
                skipBits(1);
            } // separate_colour_plane_flag

            skipUEG(); // bit_depth_luma_minus8
            skipUEG(); // bit_depth_chroma_minus8
            skipBits(1); // qpprime_y_zero_transform_bypass_flag
            if (readBoolean()) { // seq_scaling_matrix_present_flag
                scalingListCount = (chromaFormatIdc !== 3) ? 8 : 12;
                for (i = 0; i < scalingListCount; i++) {
                    if (readBoolean()) { // seq_scaling_list_present_flag[ i ]
                        if (i < 6) {
                            skipScalingList(16);
                        } else {
                            skipScalingList(64);
                        }
                    }
                }
            }
        }
        skipUEG(); // log2_max_frame_num_minus4
        let picOrderCntType = readUEG();
        if (picOrderCntType === 0) {
            readUEG(); // log2_max_pic_order_cnt_lsb_minus4
        } else if (picOrderCntType === 1) {
            skipBits(1); // delta_pic_order_always_zero_flag
            skipEG(); // offset_for_non_ref_pic
            skipEG(); // offset_for_top_to_bottom_field
            numRefFramesInPicOrderCntCycle = readUEG();
            for (i = 0; i < numRefFramesInPicOrderCntCycle; i++) {
                skipEG();
            } // offset_for_ref_frame[ i ]
        }
        skipUEG(); // max_num_ref_frames
        skipBits(1); // gaps_in_frame_num_value_allowed_flag
        picWidthInMbsMinus1 = readUEG();
        picHeightInMapUnitsMinus1 = readUEG();
        frameMbsOnlyFlag = readBits(1);
        if (frameMbsOnlyFlag === 0) {
            skipBits(1);
        } // mb_adaptive_frame_field_flag

        skipBits(1); // direct_8x8_inference_flag
        if (readBoolean()) { // frame_cropping_flag
            frameCropLeftOffset = readUEG();
            frameCropRightOffset = readUEG();
            frameCropTopOffset = readUEG();
            frameCropBottomOffset = readUEG();
        }
        let pixelRatio = [1, 1];
        if (readBoolean()) {
            // vui_parameters_present_flag
            if (readBoolean()) {
                // aspect_ratio_info_present_flag
                const aspectRatioIdc = readUByte();
                switch (aspectRatioIdc) {
                    case 1:
                        pixelRatio = [1, 1];
                        break;
                    case 2:
                        pixelRatio = [12, 11];
                        break;
                    case 3:
                        pixelRatio = [10, 11];
                        break;
                    case 4:
                        pixelRatio = [16, 11];
                        break;
                    case 5:
                        pixelRatio = [40, 33];
                        break;
                    case 6:
                        pixelRatio = [24, 11];
                        break;
                    case 7:
                        pixelRatio = [20, 11];
                        break;
                    case 8:
                        pixelRatio = [32, 11];
                        break;
                    case 9:
                        pixelRatio = [80, 33];
                        break;
                    case 10:
                        pixelRatio = [18, 11];
                        break;
                    case 11:
                        pixelRatio = [15, 11];
                        break;
                    case 12:
                        pixelRatio = [64, 33];
                        break;
                    case 13:
                        pixelRatio = [160, 99];
                        break;
                    case 14:
                        pixelRatio = [4, 3];
                        break;
                    case 15:
                        pixelRatio = [3, 2];
                        break;
                    case 16:
                        pixelRatio = [2, 1];
                        break;
                    case 255: {
                        pixelRatio = [readUByte() << 8 | readUByte(), readUByte() << 8 | readUByte()];
                        break;
                    }
                }
            }
        }
        return {
            width: Math.ceil((((picWidthInMbsMinus1 + 1) * 16) - frameCropLeftOffset * 2 - frameCropRightOffset * 2)),
            height: ((2 - frameMbsOnlyFlag) * (picHeightInMapUnitsMinus1 + 1) * 16) - ((frameMbsOnlyFlag ? 2 : 4) * (frameCropTopOffset + frameCropBottomOffset)),
            pixelRatio: pixelRatio
        };
    }

    readSliceType() {
        // skip NALu type
        this.readUByte();
        // discard first_mb_in_slice
        this.readUEG();
        // return slice_type
        return this.readUEG();
    }
}

class MSE {
    static get ErrorNotes() {
        return {
            [MediaError.MEDIA_ERR_ABORTED]: 'fetching process aborted by user',
            [MediaError.MEDIA_ERR_NETWORK]: 'error occurred when downloading',
            [MediaError.MEDIA_ERR_DECODE]: 'error occurred when decoding',
            [MediaError.MEDIA_ERR_SRC_NOT_SUPPORTED]: "audio/video not supported"
        };
    }

    static isSupported(mimeType = [MSE.CODEC_AVC_BASELINE, MSE.CODEC_AAC]) {
        return window.MediaSource && window.MediaSource.isTypeSupported('video/mp4; codecs="' + mimeType.join(",") + '"');
    }

    constructor(players) {
        this.players = players;
        this.eventSource = new EventEmitter;
        this.reset();
    }

    destroy() {
        this.clear();
        this.eventSource.destroy();
    }

    play() {
        this.players.forEach((player) => {
            player.play();
        });
    }

    resetBuffers() {
        this.players.forEach((video) => {
            video.pause();
            video.currentTime = 0;
        });
        let promises = [];
        for (let buffer of this.buffers.values()) {
            promises.push(buffer.clear());
        }
        return Promise.all(promises).then(() => {
            this.mediaSource.endOfStream();
            this.mediaSource.duration = 0;
            this.mediaSource.clearLiveSeekableRange();
            this.play();
        });
    }

    clear() {
        for (let track in this.buffers) {
            this.buffers[track]['destroy']();
            delete this.buffers[track];
        }
    }

    doCleanup() {
        for (let track in this.buffers) {
            this.buffers[track]['doCleanup']();
        }
    }

    reset() {
        this.updating = false;
        this.resolved = false;
        this.buffers = {};
        this.mediaSource = new MediaSource;
        this.players.forEach((player) => {
            player.src = URL.createObjectURL(this.mediaSource);
        });
        this.mediaReady = new Promise((resolve, reject) => {
            this.mediaSource.addEventListener('sourceopen', () => {
                //console.log('Media source opened: ' + this.mediaSource.readyState);
                if (!this.resolved) {
                    this.resolved = true;
                    resolve();
                }
            });
            this.mediaSource.addEventListener('sourceended', () => {
                // console.log('Media source ended: ' + this.mediaSource.readyState);
            });
            this.mediaSource.addEventListener('sourceclose', () => {
                //console.log('Media source closed: ' + this.mediaSource.readyState);
                this.eventSource.dispatchEvent('sourceclose');
            });
        });
    }

    setCodec(trackType, codec) {
        return this.mediaReady.then(() => {
            this.buffers[trackType] = new Buffer(this, codec);
        });
    }
    feed(trackType, moof) {
        if (this.buffers[trackType]) {
            this.buffers[trackType]['feed'](moof);
        }
    }
}

const listener = Symbol("event_listener");
const listeners = Symbol('event_listeners');

class Remuxer {
    static get TrackConverters() {
        return {
            "H264": H264TrackConverter
        };
    }

    constructor() {
        this.initialized = false;
        this.initSegment = null;
        this.tracks = {};
        this.codecs = [];
        this.streams = {};
        this.enabled = false;
        this.mse_ready = true;
        this.errorListener = this.sendTeardown.bind(this);
        this.closeListener = this.sendTeardown.bind(this);
        this.count = 0;
        this.track_type = null;
    }

    setTrack(sessionBlock) {
        // h264
        let videoEncoder = sessionBlock.rtpmap[sessionBlock.fmt[0]]['name'];
        if (Remuxer.TrackConverters[videoEncoder]) {
            this.tracks[sessionBlock.type] = new (Remuxer.TrackConverters[videoEncoder])(sessionBlock);
        } else {
            Log.warn(sessionBlock.type + ' track is not attached cause there is no remuxer for ' + videoEncoder);
        }
    }

    setTimeOffset(timeOffset, track) {
        if (this.tracks[track.type]) {
            this.tracks[track.type]['timeOffset'] = timeOffset / this.tracks[track.type]["scaleFactor"];
        }
    }

    init() {
        let tracks = [];
        let initMSE = [];
        this.codecs = [];
        for (let trackType in this.tracks) {
            let track = this.tracks[trackType];
            if (!MSE.isSupported([track.codecstring])) {
                throw new Error(track.mp4track.type + ' codec ' + track.codecstring + ' is not supported');
            }
            tracks.push(track.mp4track);
            this.codecs.push(track.codecstring);
            initMSE.push(this.initMSE(trackType));
            this.track_type = trackType;
            this.initSegment = MP4.initSegment(tracks, 9E4, 9E4);
        }
        this.initialized = true;
        Promise.all(initMSE).then(() => {
            this.mse.play();
            this.enabled = true;
        });
    }

    initMSE(trackType) {
        if (MSE.isSupported(this.codecs)) {
            return this.mse.setCodec(trackType, 'video/mp4; codecs="' + this.codecs.join(", ") + '"').then(() => {
                this.mse.feed(trackType, this.initSegment);
            });
        } else {
            throw new Error('Codecs are not supported');

            Log('Codec không hỗ trợ', "error");
        }
    }

    attachMSE(mse) {
        if (this.mse) {
            this.detachMSE();
        }
        this.mse = mse;
        this.mse.eventSource.addEventListener('error', this.errorListener);
        this.mse.eventSource.addEventListener('sourceclose', this.closeListener);
        if (this.initialized) {
            this.initMSE();
        }
    }

    detachMSE() {
        if (this.mse) {
            this.mse.eventSource.removeEventListener('error', this.errorListener);
            this.mse.eventSource.removeEventListener('sourceclose', this.closeListener);
            this.mse = null;
        }
    }

    sendTeardown() {

        Log('Xóa hết cache', 'info');
        this.mse_ready = false;
        this.enabled = false;
        this.initialized = false;
    }

    flush() {
        if (!this.mse_ready) {
            return;
        }
        if (!this.initialized) {
            for (let trackType in this.tracks) {
                if (!this.tracks[trackType]["readyToDecode"]) {
                    return;
                }
            }
            try {
                Log("Khởi tạo player thành công, sẵn sàng decode ", 'info');
                this.init();
            } catch (error) {
                this.mse.eventSource.dispatchEvent('error', {
                    "reason": error.messages
                });
                Log('Có lỗi trong quá trình khởi tạo player' + error.message, 'error');
                Log.error(error.message);
                this.sendTeardown();
                return;
            }
        }
        if (!this.enabled) {
            return;
        }
        if (this.mse) {
            for (let trackType in this.tracks) {
                let track = this.tracks[trackType];
                let payload = track.getPayload();
                if (payload && payload.byteLength) {
                    let mdat = MP4.mdat(payload);
                    let moof = MP4.moof(track.seq, track.firstDTS, track.mp4track);
                    this.mse.feed(trackType, moof);
                    this.mse.feed(trackType, mdat);
                    track.flush();
                }
            }
        } else {
            for (let trackType in this.tracks) {
                let track = this.tracks[trackType];
                track.flush();
            }
        }
    }

    feedRTP(rtp) {
        let track = this.tracks[rtp.media.type];
        if (track) {
            track.remux(rtp);
        }
    }
}

class DestructibleEventListener {
    constructor(element) {
        this[listener] = element;
        this[listeners] = new Map;
    }

    destroy() {
        this[listeners]['forEach']((listener_set, event) => {
            listener_set.forEach((fn) => {
                this[listener]['removeEventListener'](event, fn);
            });
            listener_set = null;
        });
        this[listeners] = null;
    }

    addEventListener(type, event) {
        if (!this[listeners]['has'](type)) {
            this[listeners]['set'](type, new Set);
        }
        this[listeners]['get'](type).add(event);
        this[listener]['addEventListener'](type, event, ![]);
    }

    removeEventListener(event, fn) {
        this[listener]['removeEventListener'](event, fn, ![]);
        if (this[listeners]['has'](event)) {
            this[listeners]['set'](event, new Set);
            let ev = this[listeners]['get'](event);
            ev.delete(fn);
            if (!ev.size) {
                this[listeners]['delete'](event);
            }
        }
    }

    dispatchEvent(event) {
        this[listener]['dispatchEvent'](event);
    }
}

class EventEmitter {
    constructor() {
        this.listener = new DestructibleEventListener(document.createElement('div'));
    }

    destroy() {
        this.listener.destroy();
        this.listener = null;
    }

    addEventListener(type, event) {
        this.listener.addEventListener(type, event, ![]);
    }

    removeEventListener(event, fn) {
        this.listener.removeEventListener(event, fn, ![]);
    }

    dispatchEvent(event, data) {
        this.listener.dispatchEvent(new CustomEvent(event, {
            "detail": data
        }));
    }
}

class Buffer {
    constructor(parent, codec) {
        this.mediaSource = parent.mediaSource;
        this.players = parent.players;
        this.cleaning = false;
        this.parent = parent;
        this.queue = [];
        this.cleanResolvers = [];
        this.codec = codec;
        this.check = false;
        this.flushBuffer = false;
        this.bufferFlushInterval = setInterval(() => {
            this.flushBuffer = true;
        }, 5E4);
        //console.log('Use codec: ' + _0x417c82$jscomp$0);
        this.sourceBuffer = this.mediaSource.addSourceBuffer(codec);
        this.sourceBuffer.mode = 'sequence';
        //console.log('SourceBuffer mode set to ' + this.sourceBuffer.mode);
        this.sourceBuffer.addEventListener('updatestart', (event) => {
            if (this.cleaning) {
                //console.log(this.codec + " cleaning start");
            }
        });
        this.sourceBuffer.addEventListener('update', (event) => {
            if (this.cleaning) {
                //console.log(this.codec + ' cleaning update');
            }
        });
        this.sourceBuffer.addEventListener("updateend", (event) => {
            if (this.cleaning) {
                //console.log(this.codec + ' cleaning end');
                for (; this.cleanResolvers.length;) {
                    let cleanResolver = this.cleanResolvers.shift();
                    cleanResolver();
                }
                this.cleaning = false;
            } else {
            }
            let bufferedLength = this.sourceBuffer.buffered.length;
            if (this.flushBuffer && !this.sourceBuffer.updating && bufferedLength > 0 && this.mediaSource.duration) {
                //console.log('BUFFER FLUSHING....');
                let i = 0;
                for (; i < this.sourceBuffer.buffered.length - 1; i++) {
                    if (!this.sourceBuffer.updating && this.sourceBuffer.buffered.end(i) - this.sourceBuffer.buffered.start(i) > .5) {
                        this.sourceBuffer.remove(this.sourceBuffer.buffered.start(i), this.sourceBuffer.buffered.end(i));
                    }
                }
                if (!this.sourceBuffer.updating) {
                    if (this.sourceBuffer.buffered.end(this.sourceBuffer.buffered.length - 1) - this.sourceBuffer.buffered.start(this.sourceBuffer.buffered.length - 1) > 10) {
                        this.sourceBuffer.remove(this.sourceBuffer.buffered.start(this.sourceBuffer.buffered.length - 1), this.sourceBuffer.buffered.end(this.sourceBuffer.buffered.length - 1) - 5);
                    }
                    this.flushBuffer = false;
                }
            }
            this.feedNext();
        });
        this.sourceBuffer.addEventListener("error", (event) => {
            //console.log('Source buffer error: ' + this.mediaSource.readyState);
            if (this.mediaSource.sourceBuffers.length) {
                this.mediaSource.removeSourceBuffer(this.sourceBuffer);
            }
            this.parent.eventSource.dispatchEvent('error');
        });
        this.sourceBuffer.addEventListener("abort", (event) => {
            //console.log('Source buffer aborted: ' + this.mediaSource.readyState);
            if (this.mediaSource.sourceBuffers.length) {
                this.mediaSource.removeSourceBuffer(this.sourceBuffer);
            }
            this.parent.eventSource.dispatchEvent('error');
        });
        if (!this.sourceBuffer.updating) {
            this.feedNext();
        }
    }

    destroy() {
        clearInterval(this.bufferFlushInterval);
        this.clear();
        this.mediaSource.removeSourceBuffer(this.sourceBuffer);
    }

    clear() {
        this.queue = [];
        let promises = [];
        for (let i = 0; i < this.sourceBuffer.buffered.length; ++i) {
            this.cleaning = true;
            promises.push(new Promise((resolve, reject) => {
                this.cleanResolvers.push(resolve);
            }));
        }
        return Promise.all(promises);
    }

    feedNext() {
        if (!this.sourceBuffer.updating && !this.cleaning && this.queue.length) {
            if (this.sourceBuffer.buffered.length && this.sourceBuffer.buffered.start(this.sourceBuffer.buffered.length - 1) > this.players[0]['currentTime']) {
                this.players[0]['currentTime'] = this.sourceBuffer.buffered.start(this.sourceBuffer.buffered.length - 1);
            }
            if (this.sourceBuffer.buffered.length && this.sourceBuffer.buffered["end"](this.sourceBuffer.buffered.length - 1) - this.sourceBuffer.buffered.start(this.sourceBuffer.buffered.length - 1) > 5) {
                let timeLate = this.sourceBuffer.buffered.end(this.sourceBuffer.buffered.length - 1) - this.players[0]['currentTime'];
                if (timeLate > 1) {
                    Log('Độ trễ >1s, đồng bộ lại dữ liệu debugger eval code:1:9', 'warning');
                    this.players[0]["playbackRate"] = 1.5;
                    this.players[0]['play']();
                    //console.log('speed 1.5');
                }
                if (timeLate < .5 && this.players[0]['playbackRate'] > 1) {
                    this.players[0]['playbackRate'] = 1;
                    //console.log('speed 1');
                }
            }
            if (!this.sourceBuffer.updating) {
                this.doAppend(this.queue.shift());
            }
        }
    }

    doCleanup() {
        if (this.sourceBuffer.buffered.length && !this.sourceBuffer.updating && !this.cleaning) {
        } else {
            if (!this.sourceBuffer.updating) {
                this.feedNext();
            }
        }
    }

    doAppend(data) {
        let err = this.players[0]['error'];
        if (err) {
            if (!this.sourceBuffer.updating) {
                //console.log('Error occured: ' + MSE.ErrorNotes[_0x40b4e1$jscomp$0.code]);
            }

            Log('Có lỗi trong quá trình giải mã: ' + MSE.ErrorNotes[err.code], 'error');
            try {
                this.mediaSource.endOfStream();
            } catch (e) {
            }
            this.parent.eventSource.dispatchEvent('error');
        } else {
            try {
                if (!this.sourceBuffer.updating) {
                    this.sourceBuffer.appendBuffer(data);
                }
            } catch (e) {
                if (e.name === 'QuotaExceededError') {
                    //console.log(this.codec + ' quota fail');
                    this.doCleanup();
                    return;
                }
                //console.log('Error occured while appending buffer. ' + _0x338e6d$jscomp$0.name + ": " + _0x338e6d$jscomp$0.message);

                Log('Dữ liệu decode không thể play: ' + e.name + ": " + e.message, "error");
                this.parent.eventSource.dispatchEvent("error");
            }
        }
    }

    feed(data) {
        if (this.queue.length > 100) {
            this.queue.shift();
        }
        this.queue = this.queue.concat(data);
        if (this.sourceBuffer && !this.sourceBuffer.updating) {
            this.feedNext();
        }
    }
}

class SDPParser {
    constructor() {
        this.version = -1;
        this.origin = null;
        this.sessionName = null;
        this.timing = null;
        this.sessionBlock = {};
        this.media = {};
        this.tracks = {};
        this.mediaMap = {};
    }

    parse(data) {
        return new Promise((callback, error) => {
            var originalData = data;
            var parsedCommand = true;
            var sessionBlock = this.sessionBlock;
            for (let command of originalData.split("\n")) {
                command = command.replace(/\r/, "");
                if (0 === command.length) {
                    continue;
                }
                switch (command.charAt(0)) {
                    case "v":
                        if (-1 !== this.version) {
                            //console.log("Version present multiple times in SDP");
                            error();
                            return false;
                        }
                        parsedCommand = parsedCommand && this._parseVersion(command);
                        break;
                    case "o":
                        if (null !== this.origin) {
                            console.log('Origin present multiple times in SDP');
                            error();
                            return false;
                        }
                        parsedCommand = parsedCommand && this._parseOrigin(command);
                        break;
                    case "s":
                        if (null !== this.sessionName) {
                            //console.log("Session Name present multiple times in SDP");
                            error();
                            return false;
                        }
                        command = "s=mychn";
                        parsedCommand = parsedCommand && this._parseSessionName(command);
                        break;
                    case "t":
                        if (null !== this.timing) {
                            //console.log('Timing present multiple times in SDP');
                            error();
                            return false;
                        }
                        parsedCommand = parsedCommand && this._parseTiming(command);
                        break;
                    case "m":
                        if (null !== sessionBlock && this.sessionBlock !== sessionBlock) {
                            this.media[sessionBlock.type] = sessionBlock;
                        }
                        sessionBlock = {};
                        sessionBlock.rtpmap = {};
                        this._parseMediaDescription(command, sessionBlock);
                        break;
                    case "a":
                        if (command == 'a=rtpmap:96 H264/90000 ') {
                            //console.log('a=rtpmap:96 H264/90000 ');
                        }
                        SDPParser._parseAttribute(command, sessionBlock);
                        break;
                    default:
                        //console.log('Ignored unknown SDP directive: ' + command);
                        break;
                }
                if (!parsedCommand) {
                    error();
                    return;
                }
            }
            this.media[sessionBlock.type] = sessionBlock;
            if (parsedCommand) {
                callback();
            } else {
                error();
            }
        });
    }

    _parseVersion(line) {
        var matches = line.match(/^v=([0-9]+)$/);
        if (0 === matches.length) {
            //console.log(''v=' (Version) formatted incorrectly: ' + _0x14bdb5$jscomp$0);
            return false;
        }
        this.version = matches[1];
        if (0 != this.version) {
            //console.log('Unsupported SDP version:' + this.version);
            return false;
        }
        return true;
    }

    _parseOrigin(line) {
        var matches = line.match(/^o=([^ ]+) ([0-9]+) ([0-9]+) (IN) (IP4|IP6) ([^ ]+)$/);
        if (0 === matches.length) {
            //console.log(''o=' (Origin) formatted incorrectly: ' + _0x4d444c$jscomp$0);
            return false;
        }
        this.origin = {};
        this.origin.username = matches[1];
        this.origin.sessionid = matches[2];
        this.origin.sessionversion = matches[3];
        this.origin.nettype = matches[4];
        this.origin.addresstype = matches[5];
        this.origin.unicastaddress = matches[6];
        return true;
    }

    _parseSessionName(line) {
        var matches = line.match(/^s=([^\r\n]+)$/);
        if (0 === matches.length) {
            //console.log(''s=' (Session Name) formatted incorrectly: ' + _0x4b1201$jscomp$0);
            return false;
        }
        this.sessionName = matches[1];
        return true;
    }

    _parseTiming(line) {
        var matches = line.match(/^t=([0-9]+) ([0-9]+)$/);
        if (0 === matches.length) {
            //console.log("'t=' (Timing) formatted incorrectly: " + _0x436db0$jscomp$0);
            return false;
        }
        this.timing = {};
        this.timing.start = matches[1];
        this.timing.stop = matches[2];
        return true;
    }

    _parseMediaDescription(command, sessionBlock) {
        var match = command.match(/^m=([^ ]+) ([^ ]+) ([^ ]+)[ ]/);
        if (0 === match.length) {
            //console.log("'m=' (Media) formatted incorrectly: " + command);
            return false;
        }
        sessionBlock.type = match[1];
        sessionBlock.port = match[2];
        sessionBlock.proto = match[3];
        sessionBlock.fmt = command.substr(match[0]['length']).split(" ").map(function (value) {
            return parseInt(value);
        });
        for (let fmtKey of sessionBlock.fmt) {
            this.mediaMap[fmtKey] = sessionBlock;
        }
        return true;
    }

    static _parseAttribute(line, media) {
        if (null === media) {
            return true;
        }
        var matches;
        var separator = line.indexOf(":");
        var attribute = line.substr(0, -1 === separator ? 2147483647 : separator);
        switch (attribute) {
            case 'a=recvonly':
            case "a=sendrecv":
            case 'a=sendonly':
            case 'a=inactive':
                media.mode = line.substr("a=" ['length']);
                break;
            case 'a=range':
                matches = line.match(/^a=range:\s*([a-zA-Z-]+)=([0-9.]+|now)-([0-9.]*)$/);
                media.range = [Number(matches[2] == "now" ? -1 : matches[2]), Number(matches[3]), matches[1]];
                break;
            case 'a=control':
                media.control = line.substr('a=control:' ["length"]);
                break;
            case 'a=rtpmap':
                matches = line.match(/^a=rtpmap:(\d+) (.*)$/);
                if (null === matches) {
                    //console.log('Could not parse 'rtpmap' of 'a='');
                    return false;
                }
                var payload = parseInt(matches[1]);
                media.rtpmap[payload] = {};
                var attrs = matches[2]['split']("/");
                media.rtpmap[payload]['name'] = attrs[0];
                media.rtpmap[payload]['clock'] = attrs[1];
                if (undefined !== attrs[2]) {
                    media.rtpmap[payload]["encparams"] = attrs[2];
                }
                break;
            case 'a=fmtp':
                matches = line.match(/^a=fmtp:(\d+) (.*)$/);
                if (0 === matches.length) {
                    //console.log('Could not parse 'fmtp' of 'a='');
                    return false;
                }
                media.fmtp = {};
                var param;
                for (param of matches[2]["split"](";")) {
                    var idx = param.indexOf("=");
                    media.fmtp[param.substr(0, idx).toLowerCase().trim()] = param.substr(idx + 1).trim();
                }
                break;
        }
        return true;
    }

    getSessionBlock() {
        return this.sessionBlock;
    }

    hasMedia(mediaType) {
        return this.media[mediaType] != undefined;
    }

    getMediaBlock(mediaType) {
        return this.media[mediaType];
    }

    getMediaBlockByPayloadType(pt) {
        return this.mediaMap[pt] || null;
    }

    getMediaBlockList() {
        var res = [];
        var m;
        for (m in this.media) {
            res.push(m);
        }
        return res;
    }
}

class RTP {
    constructor(pkt, sdp, timestamp64 = null) {
        let bytes = new DataView(pkt.buffer, pkt.byteOffset, pkt.byteLength);
        this.pt = 96;
        if (timestamp64) {
            this.timestamp64 = timestamp64;
        } else {
            this.timestamp64 = bytes.getFloat64(2);
        }
        this.timestamp = Math.round((this.timestamp64 >> 32) + (this.timestamp64 & 4294967295) / Math.pow(2, 32));
        this.csrcs = [];
        let pktIndex = 12;
        this.media = sdp.getMediaBlockByPayloadType(this.pt);
        this.data = pkt.subarray(pktIndex);
    }

    getPayload() {
        return this.data;
    }

    getTimestampMS() {
        return this.timestamp;
    }

    toString() {
        return "RTP(" + 'version:' + this.version + ", " + 'padding:' + this.padding + ", " + 'has_extension:' + this.has_extension + ", " + 'csrc:' + this.csrc + ", " + "marker:" + this.marker + ", " + 'pt:' + this.pt + ", " + 'sequence:' + this.sequence + ", " + "timestamp:" + this.timestamp + ", " + "ssrc:" + this.ssrc + ")";
    }

    isVideo() {
        return this.media.type == 'video';
    }

    isAudio() {
        return this.media.type == "audio";
    }
}

const UINT32_MAX = Math.pow(2, 32) - 1;

class MP4 {
    static init() {
        MP4.types = {
            avc1: [], // codingname
            avcC: [],
            btrt: [],
            dinf: [],
            dref: [],
            esds: [],
            ftyp: [],
            hdlr: [],
            mdat: [],
            mdhd: [],
            mdia: [],
            mfhd: [],
            minf: [],
            moof: [],
            moov: [],
            mp4a: [],
            '.mp3': [],
            mvex: [],
            mvhd: [],
            pasp: [],
            sdtp: [],
            stbl: [],
            stco: [],
            stsc: [],
            stsd: [],
            stsz: [],
            stts: [],
            tfdt: [],
            tfhd: [],
            traf: [],
            trak: [],
            trun: [],
            trex: [],
            tkhd: [],
            vmhd: [],
            smhd: []
        };

        let i;
        for (i in MP4.types) {
            if (MP4.types.hasOwnProperty(i)) {
                MP4.types[i] = [
                    i.charCodeAt(0),
                    i.charCodeAt(1),
                    i.charCodeAt(2),
                    i.charCodeAt(3)
                ];
            }
        }

        let videoHdlr = new Uint8Array([
            0x00, // version 0
            0x00, 0x00, 0x00, // flags
            0x00, 0x00, 0x00, 0x00, // pre_defined
            0x76, 0x69, 0x64, 0x65, // handler_type: 'vide'
            0x00, 0x00, 0x00, 0x00, // reserved
            0x00, 0x00, 0x00, 0x00, // reserved
            0x00, 0x00, 0x00, 0x00, // reserved
            0x56, 0x69, 0x64, 0x65,
            0x6f, 0x48, 0x61, 0x6e,
            0x64, 0x6c, 0x65, 0x72, 0x00 // name: 'VideoHandler'
        ]);

        let audioHdlr = new Uint8Array([
            0x00, // version 0
            0x00, 0x00, 0x00, // flags
            0x00, 0x00, 0x00, 0x00, // pre_defined
            0x73, 0x6f, 0x75, 0x6e, // handler_type: 'soun'
            0x00, 0x00, 0x00, 0x00, // reserved
            0x00, 0x00, 0x00, 0x00, // reserved
            0x00, 0x00, 0x00, 0x00, // reserved
            0x53, 0x6f, 0x75, 0x6e,
            0x64, 0x48, 0x61, 0x6e,
            0x64, 0x6c, 0x65, 0x72, 0x00 // name: 'SoundHandler'
        ]);

        MP4.HDLR_TYPES = {
            'video': videoHdlr,
            'audio': audioHdlr
        };

        let dref = new Uint8Array([
            0x00, // version 0
            0x00, 0x00, 0x00, // flags
            0x00, 0x00, 0x00, 0x01, // entry_count
            0x00, 0x00, 0x00, 0x0c, // entry_size
            0x75, 0x72, 0x6c, 0x20, // 'url' type
            0x00, // version 0
            0x00, 0x00, 0x01 // entry_flags
        ]);

        let stco = new Uint8Array([
            0x00, // version
            0x00, 0x00, 0x00, // flags
            0x00, 0x00, 0x00, 0x00 // entry_count
        ]);

        MP4.STTS = MP4.STSC = MP4.STCO = stco;

        MP4.STSZ = new Uint8Array([
            0x00, // version
            0x00, 0x00, 0x00, // flags
            0x00, 0x00, 0x00, 0x00, // sample_size
            0x00, 0x00, 0x00, 0x00 // sample_count
        ]);
        MP4.VMHD = new Uint8Array([
            0x00, // version
            0x00, 0x00, 0x01, // flags
            0x00, 0x00, // graphicsmode
            0x00, 0x00,
            0x00, 0x00,
            0x00, 0x00 // opcolor
        ]);
        MP4.SMHD = new Uint8Array([
            0x00, // version
            0x00, 0x00, 0x00, // flags
            0x00, 0x00, // balance
            0x00, 0x00 // reserved
        ]);

        MP4.STSD = new Uint8Array([
            0x00, // version 0
            0x00, 0x00, 0x00, // flags
            0x00, 0x00, 0x00, 0x01]);// entry_count

        let majorBrand = new Uint8Array([105, 115, 111, 109]); // isom
        let avc1Brand = new Uint8Array([97, 118, 99, 49]); // avc1
        let minorVersion = new Uint8Array([0, 0, 0, 1]);

        MP4.FTYP = MP4.box(MP4.types.ftyp, majorBrand, minorVersion, majorBrand, avc1Brand);
        MP4.DINF = MP4.box(MP4.types.dinf, MP4.box(MP4.types.dref, dref));
    }

    static box(type) {
        let
            payload = Array.prototype.slice.call(arguments, 1),
            size = 8,
            i = payload.length,
            len = i,
            result;
        // calculate the total size we need to allocate
        while (i--) {
            size += payload[i].byteLength;
        }

        result = new Uint8Array(size);
        result[0] = (size >> 24) & 0xff;
        result[1] = (size >> 16) & 0xff;
        result[2] = (size >> 8) & 0xff;
        result[3] = size & 0xff;
        result.set(type, 4);
        // copy the payload into the result
        for (i = 0, size = 8; i < len; i++) {
            // copy payload[i] array @ offset size
            result.set(payload[i], size);
            size += payload[i].byteLength;
        }
        return result;
    }

    static hdlr(type) {
        return MP4.box(MP4.types.hdlr, MP4.HDLR_TYPES[type]);
    }

    static mdat(data) {
        return MP4.box(MP4.types.mdat, data);
    }

    static mdhd(timescale, duration) {
        duration *= timescale;
        const upperWordDuration = Math.floor(duration / (UINT32_MAX + 1));
        const lowerWordDuration = Math.floor(duration % (UINT32_MAX + 1));
        return MP4.box(MP4.types.mdhd, new Uint8Array([
            0x01, // version 1
            0x00, 0x00, 0x00, // flags
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, // creation_time
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, // modification_time
            (timescale >> 24) & 0xFF,
            (timescale >> 16) & 0xFF,
            (timescale >> 8) & 0xFF,
            timescale & 0xFF, // timescale
            (upperWordDuration >> 24),
            (upperWordDuration >> 16) & 0xFF,
            (upperWordDuration >> 8) & 0xFF,
            upperWordDuration & 0xFF,
            (lowerWordDuration >> 24),
            (lowerWordDuration >> 16) & 0xFF,
            (lowerWordDuration >> 8) & 0xFF,
            lowerWordDuration & 0xFF,
            0x55, 0xc4, // 'und' language (undetermined)
            0x00, 0x00
        ]));
    }

    static mdia(track) {
        return MP4.box(MP4.types.mdia, MP4.mdhd(track.timescale, track.duration), MP4.hdlr(track.type), MP4.minf(track));
    }

    static mfhd(sequenceNumber) {
        return MP4.box(MP4.types.mfhd, new Uint8Array([
            0x00,
            0x00, 0x00, 0x00, // flags
            (sequenceNumber >> 24),
            (sequenceNumber >> 16) & 0xFF,
            (sequenceNumber >> 8) & 0xFF,
            sequenceNumber & 0xFF // sequence_number
        ]));
    }

    static minf(track) {
        if (track.type === 'audio') {
            return MP4.box(MP4.types.minf, MP4.box(MP4.types.smhd, MP4.SMHD), MP4.DINF, MP4.stbl(track));
        } else {
            return MP4.box(MP4.types.minf, MP4.box(MP4.types.vmhd, MP4.VMHD), MP4.DINF, MP4.stbl(track));
        }
    }

    static moof(sn, baseMediaDecodeTime, track) {
        return MP4.box(MP4.types.moof, MP4.mfhd(sn), MP4.traf(track, baseMediaDecodeTime));
    }

    /**
     * @param tracks... (optional) {array} the tracks associated with this movie
     */
    static moov(tracks) {
        let
            i = tracks.length,
            boxes = [];

        while (i--) {
            boxes[i] = MP4.trak(tracks[i]);
        }

        return MP4.box.apply(null, [MP4.types.moov, MP4.mvhd(tracks[0].timescale, tracks[0].duration)].concat(boxes).concat(MP4.mvex(tracks)));
    }

    static mvex(tracks) {
        let
            i = tracks.length,
            boxes = [];

        while (i--) {
            boxes[i] = MP4.trex(tracks[i]);
        }

        return MP4.box.apply(null, [MP4.types.mvex].concat(boxes));
    }

    static mvhd(timescale, duration) {
        duration *= timescale;
        const upperWordDuration = Math.floor(duration / (UINT32_MAX + 1));
        const lowerWordDuration = Math.floor(duration % (UINT32_MAX + 1));
        let
            bytes = new Uint8Array([
                0x01, // version 1
                0x00, 0x00, 0x00, // flags
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, // creation_time
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, // modification_time
                (timescale >> 24) & 0xFF,
                (timescale >> 16) & 0xFF,
                (timescale >> 8) & 0xFF,
                timescale & 0xFF, // timescale
                (upperWordDuration >> 24),
                (upperWordDuration >> 16) & 0xFF,
                (upperWordDuration >> 8) & 0xFF,
                upperWordDuration & 0xFF,
                (lowerWordDuration >> 24),
                (lowerWordDuration >> 16) & 0xFF,
                (lowerWordDuration >> 8) & 0xFF,
                lowerWordDuration & 0xFF,
                0x00, 0x01, 0x00, 0x00, // 1.0 rate
                0x01, 0x00, // 1.0 volume
                0x00, 0x00, // reserved
                0x00, 0x00, 0x00, 0x00, // reserved
                0x00, 0x00, 0x00, 0x00, // reserved
                0x00, 0x01, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x01, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x40, 0x00, 0x00, 0x00, // transformation: unity matrix
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, // pre_defined
                0xff, 0xff, 0xff, 0xff // next_track_ID
            ]);
        return MP4.box(MP4.types.mvhd, bytes);
    }

    static sdtp(track) {
        let
            samples = track.samples || [],
            bytes = new Uint8Array(4 + samples.length),
            flags,
            i;
        // leave the full box header (4 bytes) all zero
        // write the sample table
        for (i = 0; i < samples.length; i++) {
            flags = samples[i].flags;
            bytes[i + 4] = (flags.dependsOn << 4) |
                (flags.isDependedOn << 2) |
                (flags.hasRedundancy);
        }

        return MP4.box(MP4.types.sdtp, bytes);
    }

    static stbl(track) {
        return MP4.box(MP4.types.stbl, MP4.stsd(track), MP4.box(MP4.types.stts, MP4.STTS), MP4.box(MP4.types.stsc, MP4.STSC), MP4.box(MP4.types.stsz, MP4.STSZ), MP4.box(MP4.types.stco, MP4.STCO));
    }

    static avc1(track) {
        let sps = [], pps = [], i, data, len;
        // assemble the SPSs

        for (i = 0; i < track.sps.length; i++) {
            data = track.sps[i];
            len = data.byteLength;
            sps.push((len >>> 8) & 0xFF);
            sps.push((len & 0xFF));

            // SPS
            sps = sps.concat(Array.prototype.slice.call(data));
        }

        // assemble the PPSs
        for (i = 0; i < track.pps.length; i++) {
            data = track.pps[i];
            len = data.byteLength;
            pps.push((len >>> 8) & 0xFF);
            pps.push((len & 0xFF));

            pps = pps.concat(Array.prototype.slice.call(data));
        }

        let avcc = MP4.box(MP4.types.avcC, new Uint8Array([
                0x01, // version
                sps[3], // profile
                sps[4], // profile compat
                sps[5], // level
                0xfc | 3, // lengthSizeMinusOne, hard-coded to 4 bytes
                0xE0 | track.sps.length // 3bit reserved (111) + numOfSequenceParameterSets
            ].concat(sps).concat([
                track.pps.length // numOfPictureParameterSets
            ]).concat(pps))), // "PPS"
            width = track.width,
            height = track.height,
            hSpacing = track.pixelRatio[0],
            vSpacing = track.pixelRatio[1];

        return MP4.box(MP4.types.avc1, new Uint8Array([
                0x00, 0x00, 0x00, // reserved
                0x00, 0x00, 0x00, // reserved
                0x00, 0x01, // data_reference_index
                0x00, 0x00, // pre_defined
                0x00, 0x00, // reserved
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, // pre_defined
                (width >> 8) & 0xFF,
                width & 0xff, // width
                (height >> 8) & 0xFF,
                height & 0xff, // height
                0x00, 0x48, 0x00, 0x00, // horizresolution
                0x00, 0x48, 0x00, 0x00, // vertresolution
                0x00, 0x00, 0x00, 0x00, // reserved
                0x00, 0x01, // frame_count
                0x12,
                0x64, 0x61, 0x69, 0x6C, // dailymotion/hls.js
                0x79, 0x6D, 0x6F, 0x74,
                0x69, 0x6F, 0x6E, 0x2F,
                0x68, 0x6C, 0x73, 0x2E,
                0x6A, 0x73, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, // compressorname
                0x00, 0x18, // depth = 24
                0x11, 0x11]), // pre_defined = -1
            avcc,
            MP4.box(MP4.types.btrt, new Uint8Array([
                0x00, 0x1c, 0x9c, 0x80, // bufferSizeDB
                0x00, 0x2d, 0xc6, 0xc0, // maxBitrate
                0x00, 0x2d, 0xc6, 0xc0])), // avgBitrate
            MP4.box(MP4.types.pasp, new Uint8Array([
                (hSpacing >> 24), // hSpacing
                (hSpacing >> 16) & 0xFF,
                (hSpacing >> 8) & 0xFF,
                hSpacing & 0xFF,
                (vSpacing >> 24), // vSpacing
                (vSpacing >> 16) & 0xFF,
                (vSpacing >> 8) & 0xFF,
                vSpacing & 0xFF]))
        );
    }

    static esds(track) {
        let configlen = track.config.length;
        return new Uint8Array([
            0x00, // version 0
            0x00, 0x00, 0x00, // flags

            0x03, // descriptor_type
            0x17 + configlen, // length
            0x00, 0x01, // es_id
            0x00, // stream_priority

            0x04, // descriptor_type
            0x0f + configlen, // length
            0x40, // codec : mpeg4_audio
            0x15, // stream_type
            0x00, 0x00, 0x00, // buffer_size
            0x00, 0x00, 0x00, 0x00, // maxBitrate
            0x00, 0x00, 0x00, 0x00, // avgBitrate

            0x05 // descriptor_type
        ].concat([configlen]).concat(track.config).concat([0x06, 0x01, 0x02])); // GASpecificConfig)); // length + audio config descriptor
    }

    static mp4a(track) {
        let samplerate = track.samplerate;
        return MP4.box(MP4.types.mp4a, new Uint8Array([
                0x00, 0x00, 0x00, // reserved
                0x00, 0x00, 0x00, // reserved
                0x00, 0x01, // data_reference_index
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, // reserved
                0x00, track.channelCount, // channelcount
                0x00, 0x10, // sampleSize:16bits
                0x00, 0x00, 0x00, 0x00, // reserved2
                (samplerate >> 8) & 0xFF,
                samplerate & 0xff, //
                0x00, 0x00]),
            MP4.box(MP4.types.esds, MP4.esds(track)));
    }

    static mp3(track) {
        let samplerate = track.samplerate;
        return MP4.box(MP4.types['.mp3'], new Uint8Array([
            0x00, 0x00, 0x00, // reserved
            0x00, 0x00, 0x00, // reserved
            0x00, 0x01, // data_reference_index
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, // reserved
            0x00, track.channelCount, // channelcount
            0x00, 0x10, // sampleSize:16bits
            0x00, 0x00, 0x00, 0x00, // reserved2
            (samplerate >> 8) & 0xFF,
            samplerate & 0xff, //
            0x00, 0x00]));
    }

    static stsd(track) {
        if (track.type === 'audio') {
            if (!track.isAAC && track.codec === 'mp3') {
                return MP4.box(MP4.types.stsd, MP4.STSD, MP4.mp3(track));
            }

            return MP4.box(MP4.types.stsd, MP4.STSD, MP4.mp4a(track));
        } else {
            return MP4.box(MP4.types.stsd, MP4.STSD, MP4.avc1(track));
        }
    }

    static tkhd(track) {
        let id = track.id,
            duration = track.duration * track.timescale,
            width = track.width,
            height = track.height,
            upperWordDuration = Math.floor(duration / (UINT32_MAX + 1)),
            lowerWordDuration = Math.floor(duration % (UINT32_MAX + 1));
        return MP4.box(MP4.types.tkhd, new Uint8Array([
            0x01, // version 1
            0x00, 0x00, 0x07, // flags
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, // creation_time
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, // modification_time
            (id >> 24) & 0xFF,
            (id >> 16) & 0xFF,
            (id >> 8) & 0xFF,
            id & 0xFF, // track_ID
            0x00, 0x00, 0x00, 0x00, // reserved
            (upperWordDuration >> 24),
            (upperWordDuration >> 16) & 0xFF,
            (upperWordDuration >> 8) & 0xFF,
            upperWordDuration & 0xFF,
            (lowerWordDuration >> 24),
            (lowerWordDuration >> 16) & 0xFF,
            (lowerWordDuration >> 8) & 0xFF,
            lowerWordDuration & 0xFF,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, // reserved
            0x00, 0x00, // layer
            0x00, 0x00, // alternate_group
            0x00, 0x00, // non-audio track volume
            0x00, 0x00, // reserved
            0x00, 0x01, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x01, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x40, 0x00, 0x00, 0x00, // transformation: unity matrix
            (width >> 8) & 0xFF,
            width & 0xFF,
            0x00, 0x00, // width
            (height >> 8) & 0xFF,
            height & 0xFF,
            0x00, 0x00 // height
        ]));
    }

    static traf(track, baseMediaDecodeTime) {
        let sampleDependencyTable = MP4.sdtp(track),
            id = track.id,
            upperWordBaseMediaDecodeTime = Math.floor(baseMediaDecodeTime / (UINT32_MAX + 1)),
            lowerWordBaseMediaDecodeTime = Math.floor(baseMediaDecodeTime % (UINT32_MAX + 1));
        return MP4.box(MP4.types.traf,
            MP4.box(MP4.types.tfhd, new Uint8Array([
                0x00, // version 0
                0x00, 0x00, 0x00, // flags
                (id >> 24),
                (id >> 16) & 0XFF,
                (id >> 8) & 0XFF,
                (id & 0xFF) // track_ID
            ])),
            MP4.box(MP4.types.tfdt, new Uint8Array([
                0x01, // version 1
                0x00, 0x00, 0x00, // flags
                (upperWordBaseMediaDecodeTime >> 24),
                (upperWordBaseMediaDecodeTime >> 16) & 0XFF,
                (upperWordBaseMediaDecodeTime >> 8) & 0XFF,
                (upperWordBaseMediaDecodeTime & 0xFF),
                (lowerWordBaseMediaDecodeTime >> 24),
                (lowerWordBaseMediaDecodeTime >> 16) & 0XFF,
                (lowerWordBaseMediaDecodeTime >> 8) & 0XFF,
                (lowerWordBaseMediaDecodeTime & 0xFF)
            ])),
            MP4.trun(track,
                sampleDependencyTable.length +
                16 + // tfhd
                20 + // tfdt
                8 + // traf header
                16 + // mfhd
                8 + // moof header
                8), // mdat header
            sampleDependencyTable);
    }

    /**
     * Generate a track box.
     * @param track {object} a track definition
     * @return {Uint8Array} the track box
     */
    static trak(track) {
        track.duration = track.duration || 0xffffffff;
        return MP4.box(MP4.types.trak, MP4.tkhd(track), MP4.mdia(track));
    }

    static trex(track) {
        let id = track.id;
        return MP4.box(MP4.types.trex, new Uint8Array([
            0x00, // version 0
            0x00, 0x00, 0x00, // flags
            (id >> 24),
            (id >> 16) & 0XFF,
            (id >> 8) & 0XFF,
            (id & 0xFF), // track_ID
            0x00, 0x00, 0x00, 0x01, // default_sample_description_index
            0x00, 0x00, 0x00, 0x00, // default_sample_duration
            0x00, 0x00, 0x00, 0x00, // default_sample_size
            0x00, 0x01, 0x00, 0x01 // default_sample_flags
        ]));
    }

    static trun(track, offset) {
        let samples = track.samples || [],
            len = samples.length,
            arraylen = 12 + (16 * len),
            array = new Uint8Array(arraylen),
            i, sample, duration, size, flags, cts;
        offset += 8 + arraylen;
        array.set([
            0x00, // version 0
            0x00, 0x0f, 0x01, // flags
            (len >>> 24) & 0xFF,
            (len >>> 16) & 0xFF,
            (len >>> 8) & 0xFF,
            len & 0xFF, // sample_count
            (offset >>> 24) & 0xFF,
            (offset >>> 16) & 0xFF,
            (offset >>> 8) & 0xFF,
            offset & 0xFF // data_offset
        ], 0);
        for (i = 0; i < len; i++) {
            sample = samples[i];
            duration = sample.duration;
            size = sample.size;
            flags = sample.flags;
            cts = sample.cts;
            array.set([
                (duration >>> 24) & 0xFF,
                (duration >>> 16) & 0xFF,
                (duration >>> 8) & 0xFF,
                duration & 0xFF, // sample_duration
                (size >>> 24) & 0xFF,
                (size >>> 16) & 0xFF,
                (size >>> 8) & 0xFF,
                size & 0xFF, // sample_size
                (flags.isLeading << 2) | flags.dependsOn,
                (flags.isDependedOn << 6) |
                (flags.hasRedundancy << 4) |
                (flags.paddingValue << 1) |
                flags.isNonSync,
                flags.degradPrio & 0xF0 << 8,
                flags.degradPrio & 0x0F, // sample_flags
                (cts >>> 24) & 0xFF,
                (cts >>> 16) & 0xFF,
                (cts >>> 8) & 0xFF,
                cts & 0xFF // sample_composition_time_offset
            ], 12 + 16 * i);
        }
        return MP4.box(MP4.types.trun, array);
    }

    static initSegment(tracks) {
        if (!MP4.types) {
            MP4.init();
        }

        let movie = MP4.moov(tracks), result;
        result = new Uint8Array(MP4.FTYP.byteLength + movie.byteLength);
        result.set(MP4.FTYP);
        result.set(movie, MP4.FTYP.byteLength);
        return result;
    }
}

let track_id = 1;

class BaseRemuxer {
    static PTSNormalize(value, reference) {
        return value;
    }

    static getTrackID() {
        return track_id++;
    }

    constructor(sessionBlock) {
        this.timeOffset = 0;
        this.timescale = Number(sessionBlock.rtpmap["" + sessionBlock.fmt[0]]["clock"]);
        this.scaleFactor = (this.timescale | 0) / 1E3;
        this.readyToDecode = false;
        this.seq = 1;
    }

    msToScaled(timestamp) {
        return (timestamp - this.timeOffset) * this.scaleFactor;
    }

    remux(unit) {
        return this.timeOffset >= 0;
    }
}

class NALU {
    static get NDR() {
        return 1;
    }

    static get IDR() {
        return 5;
    }

    static get SEI() {
        return 6;
    }

    static get SPS() {
        return 7;
    }

    static get PPS() {
        return 8;
    }

    static get FU_A() {
        return 28;
    }

    static get FU_B() {
        return 29;
    }

    static get TYPES() {
        return {
            [NALU.IDR]: 'IDR',
            [NALU.SEI]: 'SEI',
            [NALU.SPS]: "SPS",
            [NALU.PPS]: "PPS",
            [NALU.NDR]: "NDR"
        };
    }

    static type(nalu) {
        if (nalu.ntype in NALU.TYPES) {
            return NALU.TYPES[nalu.ntype];
        } else {
            return 'UNKNOWN';
        }
    }

    constructor(ntype, nri, data, timestamp) {
        this.data = data;
        this.ntype = ntype;
        this.nri = nri;
        this.timestamp = timestamp;
    }

    appendData(idata) {
        this.data = appendByteArray(this.data, idata);
    }

    type() {
        return this.ntype;
    }

    getNri() {
        return this.nri >> 6;
    }

    getSize() {
        return 4 + 1 + this.data.byteLength;
    }

    getData() {
        let header = new Uint8Array(5 + this.data.byteLength);
        let view = new DataView(header.buffer);
        view.setUint32(0, this.data.byteLength + 1);
        view.setUint8(4, 0 & 128 | this.nri & 96 | this.ntype & 31);
        header.set(this.data, 5);
        return header;
    }
}

class NALUAsm {
    static get NALTYPE_FU_A() {
        return 28;
    }

    static get NALTYPE_FU_B() {
        return 29;
    }

    constructor() {
        this.nalu_l = null;
        this.nalu_t = null;
        this.dts_l = 0;
    }

    shiftTemp(nalu_l) {
        let nalu_l_tmp;
        if (this.nalu_t != null) {
            nalu_l_tmp = this.nalu_t;
            this.nalu_t = nalu_l;
        } else {
            nalu_l_tmp = nalu_l;
        }
        return nalu_l_tmp ? [nalu_l_tmp] : null;
    }

    onRTPPacket(rtp) {
        let payload = rtp.getPayload();
        let timestampMS = rtp.getTimestampMS();
        if (!rtp.media) {
            return null;
        }
        let data = new DataView(payload.buffer, payload.byteOffset);
        let nalhdr = data.getUint8(0);
        let nri = nalhdr & 96;
        let naltype = nalhdr & 31;
        let nal_start_idx = 1;
        let nalu_l = null;
        if (7 > naltype && 0 < naltype || 28 > naltype && 8 < naltype) {
            if (this.dts_l != timestampMS) {
                this.dts_l = timestampMS;
                nalu_l = this.shiftTemp(this.nalu_l);
                this.nalu_l = new NALU(naltype, nri, payload.subarray(nal_start_idx), timestampMS);
            } else {
                nalu_l = this.shiftTemp(null);
                if (this.nalu_l != null) {
                    this.nalu_l.appendData(new Uint8Array([0, 0, 1]));
                    this.nalu_l.appendData(payload.subarray(0));
                }
            }
            return nalu_l;
        } else {
            if (naltype == NALU.SPS || naltype == NALU.PPS) {
                return [new NALU(naltype, nri, payload.subarray(nal_start_idx), timestampMS)];
            } else {
                if (NALU.FU_A == naltype || NALU.FU_B == naltype) {
                    let nalfrag = data.getUint8(1);
                    let nfstart = (nalfrag & 128) >>> 7;
                    let nfend = (nalfrag & 64) >>> 6;
                    let nftype = nalfrag & 31;
                    nal_start_idx++;
                    let nfdon = 0;
                    if (NALU.FU_B === naltype) {
                        nfdon = data.getUint16(2);
                        nal_start_idx = nal_start_idx + 2;
                    }
                    if (this.dtfs_l != timestampMS) {
                        if (nfstart) {
                            nalu_l = this.shiftTemp(this.nalu_l);
                            this.nalu_l = new NALU(nftype, nri + nftype, payload.subarray(nal_start_idx), timestampMS);
                            this.dts_l = timestampMS;
                        }
                        if (this.nalu_l && this.nalu_l.ntype === nftype) {
                            if (!nfstart) {
                                this.nalu_l.appendData(payload.subarray(nal_start_idx));
                            }
                            if (nfend) {
                                nalu_l = nalu_l = this.shiftTemp(this.nalu_l);
                                this.nalu_l = null;
                            }
                        }
                    } else {
                        if (this.nalu_l != null) {
                            if (this.nalu_l.ntype == nftype) {
                                nalu_l = this.shiftTemp(null);
                                if (nfstart) {
                                    this.nalu_l.appendData(new Uint8Array([0, 0, 1, nri + nftype]));
                                    this.nalu_l.appendData(payload.subarray(nal_start_idx));
                                } else {
                                    this.nalu_l.appendData(payload.subarray(nal_start_idx));
                                }
                            } else {
                                if (nfstart) {
                                    nalu_l = this.shiftTemp(this.nalu_l);
                                    this.nalu_l = new NALU(nftype, nri + nftype, payload.subarray(nal_start_idx), timestampMS);
                                    this.dts_l = timestampMS;
                                } else {
                                    nalu_l = this.shiftTemp(null);
                                    //console.log('fu packet error');
                                }
                            }
                        } else {
                            nalu_l = this.shiftTemp(null);
                            //console.log("fu packet start without head");
                        }
                    }
                    return nalu_l;
                } else {
                    //console.log("Undefined NAL unit, type: " + _0x527c70$jscomp$0);

                    Log('*Undefined NAL unit, type:' + naltype, 'error');
                    Log('Undefined NAL unit, type: ' + naltype, 'error');
                    nalu_l = this.shiftTemp(null);
                    return nalu_l;
                }
            }
        }
    }
}

class H264TrackConverter extends BaseRemuxer {
    constructor(sessionBlock) {
        super(sessionBlock);
        this.codecstring = MSE.CODEC_AVC_BASELINE;
        this.units = [];
        this._initDTS = undefined;
        this.nextAvcDts = undefined;
        this.naluasm = new NALUAsm;
        this.readyToDecode = false;
        this.firstDTS = 0;
        this.firstPTS = 0;
        this.lastDTS = undefined;
        this.mp4track = {
            "id": BaseRemuxer.getTrackID(),
            "type": 'video',
            "nbNalu": 0,
            "fragmented": !![],
            "sps": "",
            "pps": "",
            "width": 0,
            "height": 0,
            "samples": []
        };
        if (sessionBlock.fmtp['sprop-parameter-sets']) {
            let spropParameterSetsSplit = sessionBlock.fmtp['sprop-parameter-sets']['split'](",");
            this.mp4track.pps = [new Uint8Array(base64ToArrayBuffer(spropParameterSetsSplit[1]))];
            this.parseTrackSPS(base64ToArrayBuffer(spropParameterSetsSplit[0]));
        }
        this.timeOffset = 0;
    }

    parseTrackSPS(spropParameterSets) {
        var expGolomb = new ExpGolomb(new Uint8Array(spropParameterSets));
        var sps = expGolomb.readSPS();
        this.mp4track.width = sps.width;
        this.mp4track.height = sps.height;
        this.mp4track.pixelRatio = sps.pixelRatio;
        this.mp4track.sps = [new Uint8Array(spropParameterSets)];
        this.mp4track.timescale = this.timescale;
        this.mp4track.duration = this.timescale;
        var dataview = new DataView(spropParameterSets, 1, 4);
        this.codecstring = 'avc1.';
        for (let i = 0; i < 3; i++) {
            var codec = dataview.getUint8(i).toString(16);
            if (codec.length < 2) {
                codec = "0" + codec;
            }
            this.codecstring += codec;
        }
        this.mp4track.codec = this.codecstring;
    }

    remux(rtp) {
        if (!super.remux.call(this, rtp)) {
            return;
        }
        let nalu_l = this.naluasm.onRTPPacket(rtp);
        if (nalu_l) {
            let push = false;
            nalu_l = nalu_l[0];
            switch (nalu_l.type()) {
                case NALU.NDR:
                    if (this.readyToDecode) {
                        push = true;
                    }
                    break;
                case NALU.IDR:
                    if (!this.readyToDecode) {
                        if (this.mp4track.pps && this.mp4track.sps) {
                            push = true;
                            this.readyToDecode = true;
                            if (this._initDTS === undefined) {
                                this._initPTS = this.msToScaled(nalu_l.timestamp);
                                this._initDTS = this.msToScaled(nalu_l.timestamp);
                            }
                        }
                    } else {
                        push = true;
                    }
                    break;
                case NALU.PPS:
                    if (!this.mp4track.pps) {
                        this.mp4track.pps = [new Uint8Array(nalu_l.data)];
                    }
                    break;
                case NALU.SPS:
                    if (!this.mp4track.sps) {
                        this.parseTrackSPS(nalu_l.data);
                    }
                    break;
                default:
                    push = false;
            }
            if (this.readyToDecode) {
                if (push) {
                    this.units.push(nalu_l);
                }
            }
        }
    }

    getPayload() {
        this.mp4track.len = 0;
        this.mp4track.nbNalu = 0;
        for (let unit of this.units) {
            this.mp4track.samples.push({
                "units": {
                    "units": [unit],
                    "length": unit.getSize()
                },
                "pts": this.msToScaled(unit.timestamp),
                "dts": this.msToScaled(unit.timestamp),
                "key": unit.type() == NALU.IDR
            });
            this.mp4track.len += unit.getSize();
            this.mp4track.nbNalu += 1;
        }
        let payload = new Uint8Array(this.mp4track.len);
        let byteLength = 0;
        let samples = [];
        this.mp4track.samples.sort(function (a, b) {
            return a.pts - b.pts;
        });
        let pts;
        let dts;
        let duration = 0;
        let sample;
        let lastDTS;
        for (; this.mp4track.samples.length;) {
            let mp4track = this.mp4track.samples.shift();
            let size = 0;
            for (; mp4track.units.units.length;) {
                let unit = mp4track.units.units.shift();
                let data = unit.getData();
                payload.set(data, byteLength);
                byteLength += data.byteLength;
                size += data.byteLength;
            }
            let diffPTS = mp4track.pts - this._initPTS;
            let diffDTS = mp4track.dts - this._initDTS;
            diffDTS = Math.min(diffPTS, diffDTS);
            if (lastDTS !== undefined) {
                pts = diffPTS;
                dts = diffDTS;
                duration = dts - lastDTS;
                if (duration <= 0) {
                    //console.log('invalid sample duration at PTS/DTS: ' + _0x3a5ee0$jscomp$0.pts + "/" + _0x3a5ee0$jscomp$0.dts + '|dts norm: ' + dts + '|lastDTS: ' + _0x4dc8d1$jscomp$0 + ":" + _0x498dd3$jscomp$0);
                }
            } else {
                var nextAvcDts = this.nextAvcDts;
                var avc;
                pts = diffPTS;
                dts = diffDTS;
                if (nextAvcDts) {
                    avc = Math.round(dts - nextAvcDts);
                    if (Math.abs(avc) < 600) {
                        if (avc) {
                            if (avc > 1) {
                                //console.log('AVC:' + avc + " ms hole between fragments detected,filling it");
                            } else {
                                if (avc < -1) {
                                    //console.log('AVC:' + -avc + ' ms overlapping between fragments detected');
                                }
                            }
                            dts = nextAvcDts;
                            pts = Math.max(pts - avc, dts);
                            //console.log('Video/PTS/DTS adjusted: ' + pts + "/" + dts + ",delta:" + avc);
                        }
                    }
                }
                this.firstPTS = Math.max(0, pts);
                this.firstDTS = Math.max(0, dts);
                duration = 1;
            }
            if (duration < 0) {
                duration = 0;
            }
            sample = {
                "size": size,
                "duration": duration,
                "cts": pts - dts,
                "flags": {
                    "isLeading": 0,
                    "isDependedOn": 0,
                    "hasRedundancy": 0,
                    "degradPrio": 0
                }
            };
            let flags = sample.flags;
            if (mp4track.key === !![]) {
                flags.dependsOn = 2;
                flags.isNonSync = 0;
            } else {
                flags.dependsOn = 1;
                flags.isNonSync = 1;
            }
            samples.push(sample);
            lastDTS = dts;
        }
        var durationOfLast2Index = 0;
        if (samples.length >= 2) {
            durationOfLast2Index = samples[samples.length - 2]['duration'];
            samples[0]["duration"] = durationOfLast2Index;
        }
        this.nextAvcDts = dts + durationOfLast2Index;
        if (samples.length && navigator.userAgent.toLowerCase().indexOf('chrome') > -1) {
            let flags = samples[0]["flags"];
            flags.dependsOn = 2;
            flags.isNonSync = 0;
        }
        this.mp4track.samples = samples;
        if (samples.length) {
            this.mp4track.lastDuration = (this.lastDTS || 0) + samples[samples.length - 1]['duration'];
        } else {
            this.mp4track.lastDuration = 0;
        }
        return payload;
    }

    flush() {
        this.seq++;
        this.mp4track.samples = [];
        this.units = [];
    }
}

class TypeStreaming {
    static get HLS() {
        return "hls";
    }

    static get RTP() {
        return 'rtp';
    }
}

class WebsocketTransport {
    constructor(remuxer, wsSrc, player) {
        this.remuxer = remuxer;
        this.ret = this.urlParse(wsSrc);
        if (this.ret == null) {
            return null;
        }
        this.checkonmessage = false;
        if (player) {
            let originalClass = player.parentNode.getAttribute('class').replace('loading_cam', "");
            player.parentNode.setAttribute('class', originalClass + ' loading_cam');
        }
        this.socket_url = this.ret.full;
        this.rtp_url = this.ret.fullg;
        this.host_id = this.ret.host_id;
        this.ready = this.connect();
        this.sdpfile = null;
        this.count = 0;
        this.player = player;
        var self = this;
        this.websocket.onopen = function (event) {
            self.onOpen(event);
        };
        this.websocket.onmessage = function (event) {
            self.onMessage(event);
        };
        this.websocket.onerror = function (event) {
            self.onError(event);
        };
        this.checkReconnect2s();
    }

    connect() {


        this.RTPClient = new RTPClient(this.remuxer);
        this.websocket = new WebSocket(this.socket_url);
        if (window.MozWebSocket) {
            window.WebSocket = window.MozWebSocket;
        } else {
            if (!window.WebSocket) {
                return;
            }
        }
        var self = this;
        this.websocket.onopen = function (event) {
            self.onOpen(event);
        };
        this.websocket.onclose = function (event) {
            self.onClose(event);
        };
        this.websocket.onmessage = function (event) {
            self.onMessage(event);
        };
        this.websocket.onerror = function (event) {
            self.onError(event);
        };
    }

    checkReconnect2s() {
        var self = this;
        this._reconnect = setInterval(function () {
            self.reconnect();
        }, 2E3);
    }

    reconnect() {
        if (this.websocket && this.websocket.readyState < 3) {
            return false;
        }
        this.websocket = new WebSocket(this.socket_url);
        this.websocket.binaryType = 'arraybuffer';
        if (window.MozWebSocket) {
            window.WebSocket = window.MozWebSocket;
        } else {
            if (!window.WebSocket) {
                return;
            }
        }
        var self = this;
        this.websocket.onopen = function (event) {
            self.onOpen(event);
        };
        this.websocket.onclose = function (event) {
            self.onClose(event);
        };
        this.websocket.onmessage = function (event) {
            self.onMessage(event);
        };
        this.websocket.onerror = function (event) {
            self.onError(event);
        };
    }

    onOpen(event) {
        this.websocket.binaryType = 'arraybuffer';

        Log('*Kết nối socket thành công, gửi xác thực host_id:' + this.host_id, 'info');
        Log('Kết nối socket thành công, gửi xác thực host_id:' + this.host_id, 'info');
        this.websocket.send('{ "action":"hello", "version":"2.0", "host_id":"' + this.host_id + '", "signature":"RESERVED", "timestamp":"1480371820539" }');
        this.CheckonMessage();
    }

    onClose(event) {
        if (event.change_flow) {
            clearInterval(this._reconnect);
        }
        this.count = 0;
        this.checkonmessage = false;
        if (this.player) {
            let originalClass = this.player.parentNode.getAttribute('class').replace("loading_cam", "");
            this.player.parentNode.setAttribute('class', originalClass + ' loading_cam');
        }

        Log('Không thể kết nối đến server, kiểm tra lại url', 'error');
        Log('Không thể kết nối đến server, kiểm tra lại url', 'error');
        this.onDisconnect();
    }

    onError(event) {
    }

    onDisconnect(event) {
        //console.log('DISCONNECTED SUCCESS!!');

        Log('Đóng kết nối đến socket', 'error');
        Log('Đóng kết nối đến socket', 'error');
        this.websocket.close();
        this.RTPClient.stopStreamFlush();
    }

    onMessage(event) {
        if (!this.checkonmessage && this.player) {
            let originalClass = this.player.parentNode.getAttribute('class').replace('loading_cam', "");
            this.player.parentNode.setAttribute('class', originalClass);
        }
        this.checkonmessage = true;
        this.RTPClient.flush(event.data);
    }

    urlParse(wsSrc) {
        let protocol = null;
        let regex = /^([^:]+):\/\/([^\/]+)(.*)$/;
        let match = regex.exec(wsSrc);
        if (!match) {
            Log('Lỗi URL', 'error');
            return null;
        } else {
            let ret = {};
            ret.full = wsSrc;
            ret.protocol = match[1];
            ret.urlpath = match[3];
            ret.hostname = match[2];
            let split = ret.urlpath.split("/");
            ret.host_id = split[2];
            ret.channel_id = split[3];
            if (ret.protocol == "ws") {
                protocol = 'http';
            } else {
                if (ret.protocol == 'wss') {
                    protocol = "https";
                } else {
                    return null;
                }
            }
            ret.fullg = protocol + '://' + ret.hostname + '/live/g/' + ret.channel_id + "/";
            return ret;
        }
    }

    CheckonMessage() {
        this.checkInterval = setInterval(() => {


            // this.checkonmessage
            if (!this.checkonmessage) {

                Log('Kết nối thành công nhưng không nhận được file download, kiểm tra link', 'error');
                Log('Kết nối thành công nhưng không nhận được file download, kiểm tra link', 'error');
            }
            clearInterval(this.checkInterval);
        }, 6E3);
    }
}

class RTPClient {
    constructor(remuxer) {
        this.remuxer = remuxer;
        this.sdp = new SDPParser;
        this.sps_pps = false;
        this.sps = null;
        this.pps = null;
        this.seqMap = new Map;
        _Helper._rtp_client[_Helper._rtpcount] = this;
        this._id = _Helper._rtpcount++;
        this.startFlush();
    }

    clean() {
        this.remuxer = null;
        this.sdp = null;
        _Helper._rtp_client[this._id] = null;
    }

    sdpparse(data) {
        this.sdp.parse(data).catch(function () {
            throw new Error('Failed to parse SDP');
        }).then(function () {
            return data;
        });
        this.tracks = this.sdp.getMediaBlockList();
        for (let trackType of this.tracks) {
            let sessionBlock = this.sdp.getMediaBlock(trackType);
            this.remuxer.setTrack(sessionBlock);
        }
        this.startStreamFlush();
        this.sps_pps = true;
    }

    startStreamFlush() {
        this.flushStreamInterval = setInterval(() => {
            if (this.remuxer) {
                this.remuxer.flush();
            }
        }, 400);
    }

    startFlush() {
    }

    stopStreamFlush() {


        this.remuxer.sendTeardown();
        clearInterval(this.flushInterval);
        clearInterval(this.flushStreamInterval);
        this.clean();
    }

    // rtpdownload(_0x4e5db2$jscomp$0, _0x100bef$jscomp$0) {
    //     this.seqMap.set(Number(_0x100bef$jscomp$0), null);
    //     try {
    //         let _0x3e877c$jscomp$0 = new XMLHttpRequest;
    //         _0x3e877c$jscomp$0.responseType = 'arraybuffer';
    //         _0x3e877c$jscomp$0.timeout = 5E3;
    //         _0x3e877c$jscomp$0._id = this._id;
    //         _0x3e877c$jscomp$0._seq = Number(_0x100bef$jscomp$0);
    //         _0x3e877c$jscomp$0.open("GET", _0x4e5db2$jscomp$0, !![]);
    //         _0x3e877c$jscomp$0.onload = function () {
    //             if (this.status == 200) {
    //                 if (_Helper._rtp_client[this._id]['seqMap']['has'](this._seq)) {
    //                     _Helper._rtp_client[this._id]['seqMap']["set"](this._seq, this.response);
    //                 }
    //             } else {
    //                 if (_Helper._rtp_client[this._id]['seqMap']['has'](this._seq)) {
    //                     _Helper._rtp_client[this._id]['seqMap']['delete'](this._seq);
    //                 }
    //             }
    //             _Helper._rtp_client[this._id]['flush']();
    //         };
    //         let _0xc3007e$jscomp$0 = function (_0x2360a1$jscomp$0) {
    //             if (_Helper._rtp_client[this._id]["seqMap"]['has'](this._seq)) {
    //                 _Helper._rtp_client[this._id]["seqMap"]['delete'](this._seq);
    //             }
    //             _Helper._rtp_client[this._id]['flush']();
    //         };
    //         _0x3e877c$jscomp$0.onerror = _0xc3007e$jscomp$0;
    //         _0x3e877c$jscomp$0.ontimeout = _0xc3007e$jscomp$0;
    //         _0x3e877c$jscomp$0.send(null);
    //     } catch (_0x3f73b4$jscomp$0) {
    //         if (this.seqMap.has(Number(_0x100bef$jscomp$0))) {
    //             this.seqMap.delete(Number(_0x100bef$jscomp$0));
    //         }
    //         //console.log(_0x3f73b4$jscomp$0);
    //     }
    // }

    flush(data) {
        // buffer: ArrayBufferLike, byteOffset?: number, byteLength?: number
        try {
            let originUnit8Data = new Uint8Array(data);
            // console.log("OriginUnit8Data", originUnit8Data);
            if (this.sps_pps) {
                let unit8DataSub12 = originUnit8Data.subarray(12);
                // console.log("unit8DataSub12", unit8DataSub12);
                let unit8DataSub12DataView = new DataView(unit8DataSub12.buffer, unit8DataSub12.byteOffset);
                // console.log('Data:', unit8DataSub12DataView);
                let firstUnit8DataOfSub12DataView = unit8DataSub12DataView.getUint8(0);
                let unit8DataSub2To12 = originUnit8Data.subarray(2, 10);
                let unit8DataSub2To12DataView = new DataView(unit8DataSub2To12.buffer, unit8DataSub2To12.byteOffset, unit8DataSub2To12.byteLength);
                let firstUnit8DataSub2To12DataView = unit8DataSub2To12DataView.getFloat64(0);
                if ((firstUnit8DataOfSub12DataView & 31) != 9) {
                    if ((firstUnit8DataOfSub12DataView & 31) != 1 || firstUnit8DataSub2To12DataView != this.lastTS) {
                        this.remuxer.feedRTP(new RTP(originUnit8Data, this.sdp));
                        this.lastTS = firstUnit8DataSub2To12DataView;
                    } else {
                        this.lastTS = firstUnit8DataSub2To12DataView;
                    }
                }
            } else {
                let unit8DataSub12 = originUnit8Data.subarray(12);
                let unit8DataSub12DataView = new DataView(unit8DataSub12.buffer, unit8DataSub12.byteOffset);
                let firstUnit8DataOfSub12DataView = unit8DataSub12DataView.getUint8(0);
                var check = firstUnit8DataOfSub12DataView & 31;
                if (check == 7) {
                    this.sps = u8ToBase64(unit8DataSub12);
                }
                if (check == 8) {
                    this.pps = u8ToBase64(unit8DataSub12);
                }
                if (this.pps !== null && this.sps !== null) {
                    console.log("sps", this.sps);
                    console.log("pps", this.pps);
                    let data = `v=0\no=- 0 0 IN IP4 127.0.0.1\ns=mychns\nc=IN IP4 127.0.0.1\nt=0 0\na=tool:NamVision-cdn-3.0\nm=video 0 RTP/AVP 96\na=rtpmap:96 H264/90000\na=fmtp:96 packetization-mode=1; sprop-parameter-sets=` + this.sps + "," + this.pps + '; profile-level-id=4D001E';
                    this.sdpparse(data);
                }
            }
        } catch (error) {
            return false;
        }
    }
}

class VMSPlayer {
    constructor(videoElement, options) {
        if (typeof videoElement === typeof "") {
            this.player = document.getElementById(videoElement);
        } else {
            this.player = videoElement;
        }
        var self = this;
        this.wsSrc = options.transport;
        this.errorListener = this.stop.bind(this);
        this.mse = new MSE([this.player]);
        this.setSource();
    }

    isPlaying() {
        return !(this.player.paused || this.client.paused);
    }

    setSource() {
        this.remuxer = new Remuxer;
        this.remuxer.attachMSE(this.mse);
        this.mse.eventSource.addEventListener('error', this.errorListener);
        this.transport = new WebsocketTransport(this.remuxer, this.wsSrc, this.player);
    }

    start() {
        this.client.start();
    }

    stop() {
        //console.log('Client stop');
        this.transport.onClose({
            "change_flow": !![]
        });
    }
}

let _Helper = {
    "_rtpcount": 0,
    "_rtp_client": [],
    "_seqIdx": 0,
    "_seqMap": [],
    "_version": '1.5e4'
};

function Log(message, type) {
	if(type == 'error'){
   		console.log(type + ": " + message);
   	}
};