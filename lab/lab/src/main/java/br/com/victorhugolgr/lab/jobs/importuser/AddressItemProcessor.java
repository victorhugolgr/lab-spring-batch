package br.com.victorhugolgr.lab.jobs.importuser;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import br.com.victorhugolgr.lab.domain.Address;
import br.com.victorhugolgr.lab.dto.AddressRecord;
import br.com.victorhugolgr.lab.repository.AddressRepository;
import br.com.victorhugolgr.lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressItemProcessor implements ItemProcessor<AddressRecord, Address>{

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public @Nullable Address process(AddressRecord addressRecord) throws Exception {
        boolean existsUser = userRepository.existsById(addressRecord.userId());
        if(!existsUser) {
            log.info("Usuário com ID " + addressRecord.userId() + " não existe. Ignorando endereço.");
            return null;
        }

        Optional<Address> existsAddress = addressRepository.findByUserId(addressRecord.userId());

        if(existsAddress.isPresent()) {
            log.info("Endereço para o usuário ID {} já existe. Atualizar.", addressRecord.userId());
            return Address.of(existsAddress.get());
        }

        log.info("Processando endereço para o usuário ID {}" , addressRecord.userId());
        return Address.of(addressRecord);

        
    }

}
