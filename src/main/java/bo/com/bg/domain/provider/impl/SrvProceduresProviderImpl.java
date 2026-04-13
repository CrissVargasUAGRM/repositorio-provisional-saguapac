package bo.com.bg.domain.provider.impl;

import bo.com.bg.commons.db.QueryManager;
import bo.com.bg.commons.exceptions.ProviderException;
import bo.com.bg.commons.log.LogPrinter;
import bo.com.bg.domain.config.ProviderConfigSql;
import bo.com.bg.domain.constants.SrvIntegrationConstants;
import bo.com.bg.domain.enums.MovtoStatus;
import bo.com.bg.domain.provider.SrvProceduresProvider;
import bo.com.bg.domain.provider.response.PaymentMovtoResponse;
import bo.com.bg.domain.util.JsonPayloadUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;

@Component
public class SrvProceduresProviderImpl implements SrvProceduresProvider {

    private static final Logger logger = LoggerFactory.getLogger(SrvProceduresProviderImpl.class);

    private final QueryManager<?> queryManager;
    private final ProviderConfigSql config;
    private final ObjectMapper objectMapper;

    public SrvProceduresProviderImpl(QueryManager<?> queryManager, ProviderConfigSql config, ObjectMapper objectMapper) {
        this.queryManager = queryManager;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveInformationService(Object payload, Integer serviceId, String index) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("pIntTipoIntegracion", SrvIntegrationConstants.TYPE_INTEGRATION);
            params.put("pIntTipoColeccion", SrvIntegrationConstants.TYPE_COLLECTION_PGL_TRAZAS);
            params.put("pStrIndiceColeccion", index);
            params.put("pIntCodExterno", serviceId);
            params.put("pClobColeccion", JsonPayloadUtil.objectToString(objectMapper, payload));
            params.put("pIntEstado", MovtoStatus.ACTIVO.getCode());
            params.put("pIntSucursalRegistro", SrvIntegrationConstants.COD_SUCURSAL);

            SqlParameter[] sqlParams = {
                new SqlParameter("pIntTipoIntegracion", Types.NUMERIC),
                new SqlParameter("pIntTipoColeccion", Types.NUMERIC),
                new SqlParameter("pStrIndiceColeccion", Types.VARCHAR),
                new SqlParameter("pIntCodExterno", Types.NUMERIC),
                new SqlParameter("pClobColeccion", Types.CLOB),
                new SqlParameter("pIntEstado", Types.NUMERIC),
                new SqlParameter("pIntSucursalRegistro", Types.NUMERIC),
                new SqlOutParameter("pStrCodError", Types.VARCHAR),
                new SqlOutParameter("pStrDescError", Types.VARCHAR),
                new SqlOutParameter("pIntIdentificador", Types.NUMERIC),
            };

            PaymentMovtoResponse responseData = this.queryManager.executeSPForObject(
                    this.config.getSchemaGanadero(),
                    this.config.getPackageSrvName(),
                    this.config.getSpSrvInsertName(),
                    params,
                    sqlParams,
                    PaymentMovtoResponse.class);

            if (responseData != null
                    && responseData.getPStrCodError() != null
                    && !Objects.equals(responseData.getPStrCodError(), "COD000")) {
                String code = responseData.getPStrCodError();
                String desc = responseData.getPStrDescError() != null ? responseData.getPStrDescError() : "";
                logger.info("Error al ejecutar SP guardado de log movimientos : {}", code + " : " + desc);
            }
        } catch (ProviderException e) {
            LogPrinter.write(e);
        }
    }
}
