package com.flipperdevices.nfceditor.impl.model

import androidx.compose.runtime.Stable
import com.flipperdevices.core.ui.hexkeyboard.ImmutableEnumMap

@Stable
data class NfcEditorCardInfo(
    val cardType: NfcEditorCardType,
    val fields: ImmutableEnumMap<CardFieldInfo, List<NfcEditorCell>>
) {
    val fieldsAsSectors: List<NfcEditorSector> by lazy {
        val lines = CardFieldInfo.values().sortedBy { it.index }.map {
            NfcEditorLine(it.index, fields[it])
        }
        listOf(NfcEditorSector(lines))
    }

    fun copyWithChangedContent(
        location: NfcEditorCellLocation,
        content: String
    ): NfcEditorCardInfo {
        val fieldInfo = CardFieldInfo.byIndex(location.lineIndex) ?: return this
        val newLine = fields[fieldInfo].toMutableList()
        newLine[location.columnIndex] = newLine[location.columnIndex].copy(
            content = content
        )
        return copy(
            fields = ImmutableEnumMap(CardFieldInfo::class.java, CardFieldInfo.values()) {
                if (it == fieldInfo) newLine else fields[it]
            }
        )
    }
}

enum class NfcEditorCardType {
    MF_1K,
    MF_4K
}

enum class CardFieldInfo(val index: Int) {
    UID(index = 0),
    ATQA(index = 1),
    SAK(index = 2);

    companion object {
        fun byIndex(fieldIndex: Int): CardFieldInfo? = values().find { fieldIndex == it.index }
    }
}
