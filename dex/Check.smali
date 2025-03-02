.class public Lcom/example/Check;
.super Ljava/lang/Object;

.method public static final getFlag()Ljava/lang/String;
    .registers 6

    .line 54
    nop

    .line 55
    nop

    .line 54
    nop

    .line 55
    nop

    .line 54
    nop

    .line 55
    nop

    .line 54
    nop

    .line 55
    nop

    .line 54
    nop

    .line 55
    nop

    .line 54
    nop

    .line 55
    nop

    .line 54
    nop

    .line 55
    nop

    .line 54
    nop

    .line 55
    nop

    .line 54
    nop

    .line 56
    nop

    .line 54
    nop

    .line 56
    nop

    .line 54
    nop

    .line 56
    nop

    .line 54
    nop

    .line 56
    nop

    .line 54
    nop

    .line 56
    nop

    .line 54
    nop

    .line 56
    nop

    .line 54
    nop

    .line 56
    nop

    .line 54
    nop

    .line 56
    nop

    .line 54
    nop

    .line 57
    nop

    .line 54
    nop

    .line 57
    nop

    .line 54
    nop

    .line 57
    nop

    .line 54
    nop

    .line 57
    nop

    .line 54
    nop

    .line 57
    nop

    .line 54
    nop

    .line 57
    nop

    .line 54
    nop

    .line 57
    nop

    .line 54
    nop

    .line 57
    nop

    .line 54
    nop

    .line 58
    const/16 v0, 0x21

    new-array v0, v0, [I

    fill-array-data v0, :array_52

    .line 54
    nop

    .line 53
    nop

    .line 60
    .local v0, "encoded":[I
    const/16 v1, 0x2a

    .line 62
    .local v1, "key":I
    array-length v2, v0

    new-array v2, v2, [C

    .line 63
    .local v2, "decodedChars":[C
    const/4 v3, 0x0

    .local v3, "i":I
    array-length v4, v0

    :goto_41
    if-ge v3, v4, :cond_4c

    .line 65
    aget v5, v0, v3

    xor-int/2addr v5, v1

    int-to-char v5, v5

    aput-char v5, v2, v3

    .line 63
    add-int/lit8 v3, v3, 0x1

    goto :goto_41

    .line 68
    .end local v3    # "i":I
    :cond_4c
    new-instance v3, Ljava/lang/String;

    invoke-direct {v3, v2}, Ljava/lang/String;-><init>([C)V

    return-object v3

    :array_52
    .array-data 4
        0x62
        0x6b
        0x69
        0x61
        0x7a
        0x6b
        0x69
        0x61
        0x69
        0x7e
        0x6c
        0x78
        0x6b
        0x64
        0x6e
        0x65
        0x67
        0x6d
        0x6f
        0x64
        0x6f
        0x78
        0x6b
        0x7e
        0x6f
        0x1b
        0x18
        0x19
        0x1e
        0x1f
        0x1c
        0x1d
        0xb
    .end array-data
.end method
